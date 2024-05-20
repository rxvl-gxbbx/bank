package com.rxvlvxr.bank.domain.phone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rxvlvxr.bank.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(String number, LocalDateTime createdAt) {
        this.number = number;
        this.createdAt = createdAt;
    }

    public Phone(String number, LocalDateTime createdAt, User user) {
        this.number = number;
        this.createdAt = createdAt;
        this.user = user;
    }
}
