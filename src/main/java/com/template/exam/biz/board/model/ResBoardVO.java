package com.template.exam.biz.board.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResBoardVO {
    public int seq;
    public String title;
    public String content;
    public String userId;
    public String userName;
    public String useYn;
    public String fileId;
    public Timestamp createTime;
    public Timestamp updateTime;
}
