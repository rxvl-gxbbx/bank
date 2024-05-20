package com.rxvlvxr.bank.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rxvlvxr.bank.domain.account.Account;
import com.rxvlvxr.bank.domain.email.Email;
import com.rxvlvxr.bank.domain.phone.Phone;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "full_name")
    private String fullName;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NaturalId
    @Column(name = "login")
    private String login;
    @JsonIgnore
    @Column(name = "hash_password")
    private String hashPassword;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST})
    private Account account;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Phone> phones;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Email> emails;

    public User(String fullName, LocalDate birthDate, String login, String hashPassword, Account account, List<Phone> phones, List<Email> emails) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.login = login;
        this.hashPassword = hashPassword;
        this.account = account;
        this.phones = phones;
        this.emails = emails;
    }

    public User(String fullName, LocalDate birthDate, String login, String hashPassword) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.login = login;
        this.hashPassword = hashPassword;
    }

    public User(String fullName, LocalDate birthDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
    }
}
