package com.renato.springbootstrap.security.repository;

import com.renato.springbootstrap.security.repository.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveSortingRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);
}
