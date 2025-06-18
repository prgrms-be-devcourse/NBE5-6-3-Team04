package com.grepp.nbe563team04.app.model.auth.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserBlackList {

    @Id
    private String email;
    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public UserBlackList(String email) {
        this.email = email;
    }
}
