package com.insert.view.infra.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViewException extends RuntimeException{
    private final ErrorCode errorCode;
}
