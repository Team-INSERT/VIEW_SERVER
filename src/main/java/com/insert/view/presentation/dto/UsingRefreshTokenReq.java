package com.insert.view.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UsingRefreshTokenReq {
    @NotNull
    private String refreshToken;
}