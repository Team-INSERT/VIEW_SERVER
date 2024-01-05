package com.insert.view.service.auth.implement;

import com.insert.view.domain.auth.AuthId;
import com.insert.view.domain.auth.RefreshToken;
import com.insert.view.domain.auth.repo.AuthIdRepo;
import com.insert.view.domain.auth.repo.RefreshTokenRepo;
import com.insert.view.domain.user.User;
import com.insert.view.domain.user.authority.Authority;
import com.insert.view.domain.user.repo.UserRepo;
import com.insert.view.infra.error.exception.ErrorCode;
import com.insert.view.infra.error.exception.ViewException;
import com.insert.view.infra.jwt.TokenResponseDto;
import com.insert.view.infra.jwt.properties.JwtProperties;
import com.insert.view.infra.jwt.util.JwtProvider;
import com.insert.view.infra.jwt.util.JwtUtil;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import leehj050211.bsmOauth.exception.BsmOAuthCodeNotFoundException;
import leehj050211.bsmOauth.exception.BsmOAuthInvalidClientException;
import leehj050211.bsmOauth.exception.BsmOAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthImplement {
    private final BsmOauth bsmOauth;
    private final UserRepo userRepo;
    private final AuthIdRepo authIdRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final JwtUtil jwtUtil;

    public User appendOrUpdateUser(String authId) throws IOException {
        String token;
        BsmUserResource resource;
        try {
            token = bsmOauth.getToken(authId);
            resource = bsmOauth.getResource(token);
        } catch (BsmOAuthCodeNotFoundException | BsmOAuthTokenNotFoundException e) {
            throw new ViewException(ErrorCode.USER_NOT_LOGIN);
        } catch (BsmOAuthInvalidClientException e) {
            throw new ViewException(ErrorCode.BSM_AUTH_INVALID_CLIENT);
        }
        return checkUserExist(resource);
    }

    public User checkUserExist(final BsmUserResource resource) {
        Optional<User> user = userRepo.findById(resource.getUserCode());

        if (user.isEmpty())
            return SaveNewUser(resource);

        return UpdateUserProfile(user.get(), resource);
    }

    private User SaveNewUser(final BsmUserResource resource) {
        User user = User.builder()
                .id(resource.getUserCode())
                .email(resource.getEmail())
                .nickname(resource.getNickname())
                .authority(Authority.USER)
                .profile_image(resource.getProfileUrl())
                .build();

        switch (resource.getRole()) {
            case STUDENT -> user.setStudentValue(resource.getStudent());
            case TEACHER -> user.setTeacherValue(resource.getTeacher());
        }

        return userRepo.save(user);
    }

    public User UpdateUserProfile(User user, BsmUserResource resource) {
        user.updateUserProfile(resource);
        return user;
    }

    public void saveUserSignIn(String email) {
        authIdRepo.save(
                AuthId.builder()
                        .id(email)
                        .authId(email)
                        .ttl(jwtProperties.getRefreshExp())
                        .build()
        );
    }

    public TokenResponseDto generateToken(User user) {
        return jwtProvider.generateToken(user.getEmail(), user.getAuthority().name());
    }

    public RefreshToken readRefreshToken(String refreshToken) {
        return refreshTokenRepo.findByRefreshToken(
                jwtUtil.replaceBearer(refreshToken)
        ).orElseThrow(() -> new ViewException(ErrorCode.REFRESH_TOKEN_EXPIRED));
    }

    public TokenResponseDto getNewAccessTokens(final RefreshToken redisRefreshToken) {
        String newAccessToken = jwtProvider.generateAccessToken(redisRefreshToken.getId(), redisRefreshToken.getRole());

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(redisRefreshToken.getRefreshToken())
                .expiredAt(redisRefreshToken.getExpiredAt())
                .build();
    }

    public void removeUserSignIn(String authId) {
        authIdRepo.findByAuthId(authId)
                .ifPresent(authIdRepo::delete);
    }

    public void expireRefreshToken(String authId) {
        refreshTokenRepo.findById(authId)
                .ifPresent(refreshTokenRepo::delete);
    }
}
