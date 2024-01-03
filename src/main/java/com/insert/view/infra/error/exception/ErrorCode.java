package com.insert.view.infra.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    //User
    USER_NOT_LOGIN(403, "USER-403-1", "User Not Login"),
    USER_NOT_FOUND(404, "USER-404-1", "User Not Found"),

    //ServerError
    INVALID_ARGUMENT(400, "ARG-400-1", "Arg Is Not Valid"),
    FORBIDDEN(403, "COMMON-403-1", "Forbidden"),
    NOT_FOUND(404, "NOT_FOUND", "Not Found"),
    BSM_AUTH_INVALID_CLIENT(500, "BSM-500-1", "Bsm Client Is Invalid"),
    INTERNAL_SERVER_ERROR(500, "SERVER-500-1", "Internal Server Error"),

    //JWT
    INVALID_TOKEN(403, "TOKEN-403-1", "Access with Invalid Token"),
    EXPIRED_JWT(403, "TOKEN-403-2", "Access Token Expired"),
    REFRESH_TOKEN_EXPIRED(403, "TOKEN-403-3", "Refresh Token Expired");

    private final int status;
    private final String code;
    private final String message;
}
