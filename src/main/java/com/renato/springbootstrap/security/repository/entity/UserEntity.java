package com.renato.springbootstrap.security.repository.entity;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("users")
@Builder(toBuilder = true)
public record UserEntity(
        @Id
        Long id,
        String username,
        String email,
        String password,
        @Column("created_at")
        Instant createdAt,
        @Column("updated_at")
        Instant updateAt
) {}
