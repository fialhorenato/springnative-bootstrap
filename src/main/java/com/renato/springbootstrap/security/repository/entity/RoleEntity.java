package com.renato.springbootstrap.security.repository.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("roles")
@Builder(toBuilder = true)
public record RoleEntity(
        @Id
        Long id,
        @Column("user_id")
        Long userId,
        String role,

        @Column("created_at")
        Instant createdAt,

        @Column("updated_at")
        Instant updateAt
) {}