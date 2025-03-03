package com.template.exam.biz.common.model;

import lombok.Data;

import java.util.Set;

@Data
public class SignupVO {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private Set<String> roles;
}
