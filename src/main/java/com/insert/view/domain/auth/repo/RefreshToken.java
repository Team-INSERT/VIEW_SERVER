package com.insert.view.domain.auth.repo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshToken extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findById(String authId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
