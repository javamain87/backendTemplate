package com.template.exam.biz.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponsVO {

    private String statusCode;
    private List<Map> responseList;
    private Map responseMap;
}
