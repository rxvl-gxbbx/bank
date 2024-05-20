package com.rxvlvxr.bank.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rxvlvxr.bank.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "account", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "amount")
    private double amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "init_deposit")
    private double initDeposit;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Account(double amount, LocalDateTime createdAt, double initDeposit) {
        this.amount = amount;
        this.createdAt = createdAt;
        this.initDeposit = initDeposit;
    }

    public Account(double amount, LocalDateTime createdAt, double initDeposit, User user) {
        this.amount = amount;
        this.createdAt = createdAt;
        this.initDeposit = initDeposit;
        this.user = user;
    }
}
