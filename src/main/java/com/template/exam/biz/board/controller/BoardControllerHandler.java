package com.template.exam.biz.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.exam.biz.board.model.BoardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardControllerHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    public boolean paramCheck(Object obj) {
        Map map = objectMapper.convertValue(obj, Map.class);
        return map.values().stream().allMatch(Objects::isNull);
    }

    public Map convertMap(BoardVO reqVO) {
        return objectMapper.convertValue(reqVO, Map.class);
    }


}
