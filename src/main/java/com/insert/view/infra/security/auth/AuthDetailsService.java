package com.insert.view.infra.security.auth;

import com.insert.view.domain.user.repo.UserRepo;
import com.insert.view.infra.error.exception.ErrorCode;
import com.insert.view.infra.error.exception.ViewException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthDetailsService implements UserDetailsService {
    private final UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .map(AuthDetails::new)
                .orElseThrow(() -> new ViewException(ErrorCode.NOT_FOUND));
    }
}
