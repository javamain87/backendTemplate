package com.template.exam.biz.auth.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseVO {

    private String statusCode;
    private List<Map> responseList;
    private Map responseMap;
}
