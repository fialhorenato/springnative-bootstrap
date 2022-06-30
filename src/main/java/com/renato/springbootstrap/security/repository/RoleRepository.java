package com.renato.springbootstrap.security.repository;

import com.renato.springbootstrap.security.repository.entity.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface RoleRepository extends ReactiveSortingRepository<RoleEntity, Long> {
    Flux<RoleEntity> findByUserId(Long userId);
}
