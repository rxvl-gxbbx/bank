package com.rxvlvxr.bank.app.impl.user;

import com.rxvlvxr.bank.adapter.persistence.user.UserJpaSpecification;
import com.rxvlvxr.bank.adapter.rest.user.dto.FilterDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.PageParamsDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.SortParamsDto;
import com.rxvlvxr.bank.app.api.user.FindUsersByFiltersInbound;
import com.rxvlvxr.bank.app.api.user.UserRepository;
import com.rxvlvxr.bank.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUsersByFiltersUseCase implements FindUsersByFiltersInbound {
    private static final String[] NESTED_FIELDS = {
            "phones.number",
            "emails.address"
    };
    private final UserRepository userRepository;

    @Override
    public List<User> execute(User user, FilterDto filter) {
        SortParamsDto sort = filter.getSort();

        if (sort != null) {
            if (NESTED_FIELDS[0].endsWith(sort.getField()))
                sort.setField(NESTED_FIELDS[0]);
            if (NESTED_FIELDS[1].endsWith(sort.getField()))
                sort.setField(NESTED_FIELDS[1]);
        }

        Specification<User> specification = new UserJpaSpecification(user);
        Pageable pageable = getPageable(filter.getSort(), filter.getPagination());

        return userRepository.findAll(specification, pageable).getContent();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Pageable getPageable(SortParamsDto sortParamsDto, PageParamsDto pageParamsDto) {
        String sortField = sortParamsDto != null && sortParamsDto.getField() != null ? sortParamsDto.getField() : "fullName";
        Sort.Direction sortDirection = sortParamsDto != null && sortParamsDto.getDirection() != null ? Sort.Direction.fromString(sortParamsDto.getDirection()) : Sort.Direction.ASC;

        int pageNumber = pageParamsDto != null && pageParamsDto.getPageNumber() != null ? pageParamsDto.getPageNumber() : 1;
        int pageSize = pageParamsDto != null && pageParamsDto.getPageSize() != null ? pageParamsDto.getPageSize() : 10;

        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));
    }
}
