package com.template.exam.biz.auth.mapper;

import com.template.exam.biz.auth.model.SignupVO;
import com.template.exam.biz.auth.model.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    int signup(SignupVO reqVO);
    void insertUserRoles(SignupVO reqVO);
    UserVO findByEmail(String email);
    UserVO findByUserId(String userId);
    UserVO userAndRole(String email);
}
