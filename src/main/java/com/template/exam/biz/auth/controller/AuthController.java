package com.template.exam.biz.auth.controller;

import com.template.exam.biz.auth.model.ResponseVO;
import com.template.exam.biz.auth.model.SignupVO;
import com.template.exam.biz.auth.model.UserVO;
import com.template.exam.biz.auth.service.AuthService;
import com.template.exam.biz.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthControllerHelper authControllerHelper;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<ResponseVO> signup(@RequestBody SignupVO signupVO) {
        if (!authControllerHelper.modelCheck(signupVO)) {
            authService.signUp(signupVO);
        }
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVO userVO) throws Exception {
        authService.signIn(userVO,response);
        return ResponseEntity.status(200).body("로그인에 성공 하였습니다.");
    }

    @PostMapping("/validation")
    public ResponseEntity<Boolean> validationToken(HttpServletRequest request, HttpServletResponse response) {
        boolean result = authControllerHelper.validateToken(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @PostMapping("/refresh")
//    public void refreshToken() {
//
//    }
}
