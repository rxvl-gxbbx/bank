package com.rxvlvxr.bank.domain.email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rxvlvxr.bank.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "address")
    private String address;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Email(String address) {
        this.address = address;
    }

    public Email(String address, LocalDateTime createdAt) {
        this.address = address;
        this.createdAt = createdAt;
    }

    public Email(String address, LocalDateTime createdAt, User user) {
        this.address = address;
        this.createdAt = createdAt;
        this.user = user;
    }
}
