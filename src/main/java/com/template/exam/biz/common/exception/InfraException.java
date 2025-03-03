package com.template.exam.biz.common.exception;

import lombok.Getter;

@Getter
public class InfraException extends RuntimeException {

    private String code;

    public InfraException(String message) {
        super(message);
    }

    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfraException(String code, String message) {
        super(message);
        this.code = code;
    }
    public InfraException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
