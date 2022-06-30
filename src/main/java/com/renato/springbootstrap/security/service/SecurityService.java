package com.renato.springbootstrap.security.service;

import com.renato.springbootstrap.security.config.JwtTokenProvider;
import com.renato.springbootstrap.security.repository.RoleRepository;
import com.renato.springbootstrap.security.repository.UserRepository;
import com.renato.springbootstrap.security.repository.entity.RoleEntity;
import com.renato.springbootstrap.security.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(this::transform);
    }

    private Mono<UserDetails> transform(UserEntity userEntity) {
        return roleRepository.findByUserId(userEntity.id())
                .collectList()
                .map(it -> toUserDetails(it, userEntity));
    }

    private UserDetails toUserDetails(List<RoleEntity> roleEntities, UserEntity userEntity) {
        var roles = roleEntities.stream().map(RoleEntity::role).toList();
        var authorities = roleEntities.stream().map(it -> new SimpleGrantedAuthority("ROLE_" + it)).toList();

        return new com.renato.springbootstrap.security.service.UserDetails(
                userEntity.username(),
                userEntity.password(),
                authorities,
                roles,
                userEntity.email()
        );
    }


    public Mono<String> authenticate(String username, String password) {
        return findByUsername(username)
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .map(jwtTokenProvider::generateJwtToken);
    }
    public Mono<UserEntity> createUser(String username, String password, String email) {
        var userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createdAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        var savedUser = userRepository.save(userEntity);

        return savedUser.doOnNext(this::saveRole);
    }

    private void saveRole(UserEntity userEntity) {
        var userRole = RoleEntity.builder()
                .userId(userEntity.id())
                .role("USER")
                .createdAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        roleRepository.save(userRole).subscribe();
    }
}
