package com.template.exam.biz.common.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponseDTO {
    public String accessToken;
    public String refreshToken;

}
