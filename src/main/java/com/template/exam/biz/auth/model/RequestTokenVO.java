package com.template.exam.biz.auth.model;

import lombok.Data;

@Data
public class RequestTokenVO {
    private String accessToken;
    private String requestToken;
}
