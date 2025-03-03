package com.template.exam.biz.auth.model;

import lombok.Data;

@Data
public class ResponseTokenVO {
    private String refreshToken;
    private String accessToken;
}
