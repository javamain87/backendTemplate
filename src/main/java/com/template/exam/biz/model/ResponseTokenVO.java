package com.template.exam.biz.model;

import lombok.Data;

@Data
public class ResponseTokenVO {
    private String refreshToken;
    private String accessToken;
}
