package com.template.exam.biz.auth.model;

import lombok.Data;

import java.util.Set;

@Data
public class SignupVO {
    private int seq;
    private String userId;
    private String userName;
    private String password;
    private String email;
    private Set<String> roles;
}
