package com.template.exam.biz.auth.service;

import com.template.exam.biz.auth.mapper.AuthMapper;
import com.template.exam.biz.auth.model.SignupVO;
import com.template.exam.biz.auth.model.UserVO;
import com.template.exam.biz.common.jwt.JwtTokenProvider;
import com.template.exam.biz.common.jwt.dto.JwtResponseDTO;
import com.template.exam.biz.common.jwt.service.CustomUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Transactional
    public void signUp(SignupVO signupVO) {
        signupVO.setPassword(passwordEncoder.encode(signupVO.getPassword()));
        try {
            UserVO userUseVal = authMapper.findByUserId(signupVO.getUserId());
            if (userUseVal  ==  null) {
                int result01 = authMapper.signup(signupVO);
                if (result01 == 1) {
                    authMapper.insertUserRoles(signupVO);
                }

            } else {
                throw new Exception("이미 유저가 존재 합니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public JwtResponseDTO signIn(UserVO userVO, HttpServletResponse  response) throws Exception {
        String email = userVO.getEmail();
        UserVO user = authMapper.findByEmail(email);
        JwtResponseDTO tokenVO = null;
        if (user != null) {
            boolean matchPwd = passwordEncoder.matches(userVO.getPassword(), user.getPassword());
            if (matchPwd) {
                UserVO userAndRole = authMapper.userAndRole(userVO.getEmail());
                tokenVO = jwtTokenProvider.createToken(user,customUserDetailService.mapRolesToAuthorities(userAndRole.getRoles()));
                response.setHeader("Authorization", tokenVO.getAccessToken());
            }
        } else {
            throw new Exception("가입 되어 있는 아이디가 없습니다.");
        }
        return tokenVO;

    }
}
