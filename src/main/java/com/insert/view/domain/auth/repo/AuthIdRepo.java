package com.insert.view.domain.auth.repo;

import com.insert.view.domain.auth.AuthId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthIdRepo extends CrudRepository<AuthId, String> {
    Optional<AuthId> findByAuthId(String authId);
}
