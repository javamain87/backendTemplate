package com.template.exam.biz.board.model;

import lombok.Data;

import java.util.List;

@Data
public class ResListVO {
    private List<ResBoardVO> list;
    private int totalCnt;

}
