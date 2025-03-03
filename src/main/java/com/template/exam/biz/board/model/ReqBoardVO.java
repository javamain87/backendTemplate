package com.template.exam.biz.board.model;

import lombok.Data;

@Data
public class ReqBoardVO {

    public int seq;
    public String title;
    public String content;
    public String userId;
    public String userName;
    public String useYn;
    public String fileId;
    public int pageNum;
    public int groupNum;
}
