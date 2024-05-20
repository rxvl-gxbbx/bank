package com.rxvlvxr.bank.adapter.persistence.user;

import com.rxvlvxr.bank.domain.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class UserJpaSpecification implements Specification<User> {
    private User user;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (user.getBirthDate() != null)
            p = criteriaBuilder.and(p, criteriaBuilder.greaterThan(root.get("birthDate"), user.getBirthDate()));
        if (user.getPhones() != null && !user.getPhones().isEmpty())
            p = criteriaBuilder.and(p, criteriaBuilder.equal(root.join("phones").get("number"), user.getPhones().get(0).getNumber()));
        if (user.getFullName() != null && !user.getFullName().isBlank())
            p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("fullName"), user.getFullName() + "%"));
        if (user.getEmails() != null && !user.getEmails().isEmpty())
            p = criteriaBuilder.and(p, criteriaBuilder.equal(root.join("emails").get("address"), user.getEmails().get(0).getAddress()));

        return p;
    }
}