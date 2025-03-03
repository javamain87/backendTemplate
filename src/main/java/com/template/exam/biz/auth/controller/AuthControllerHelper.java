package com.template.exam.biz.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.exam.biz.auth.model.ResponseTokenVO;
import com.template.exam.biz.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthControllerHelper {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public boolean modelCheck(Object obj) {
        Map<String,Object> map = objectMapper.convertValue(obj, Map.class);
        return map.values().stream().allMatch(Objects::isNull);
    }

    public boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseTokenVO dto = new ResponseTokenVO();
        int result = jwtTokenProvider.validateToken(request.getHeader("Authorization"));
        if (result == 1) {
            response.setHeader("Authorization", "Bearer " + request.getHeader("Authorization"));
            return true;
        } else {
            response.setHeader("Authorization", "");
            return false;
        }
    }
}
