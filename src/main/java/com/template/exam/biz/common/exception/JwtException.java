package com.template.exam.biz.common.exception;

import lombok.Getter;

@Getter
public enum JwtException {


    TOKEN_EXPIRED_EXCEPTION("T0001","만료된 토큰 입니다."),
    SIGNATURE_VERIFICATION_EXCEPTION("T0002","알고리즘이 일치 하지 않습니다."),
    ALGORITHM_MISMATCH_EXCEPTION("T0003","유효 하지 않은 클레임 입니다."),
    INVALID_CLAIM_EXCEPTION("T0004","토큰 검증 중 예외가 발생 했습니다."),
    UNDEFINED_EXCEPTION("T0005","토큰 검증 중 예외가 발생 했습니다.");


    private final String code;
    private final String message;

    JwtException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
