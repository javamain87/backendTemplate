package com.template.exam.biz.board.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardVO {

    public int seq;
    public String title;
    public String content;
    public String userId;
    public String userName;
    public String fileId;
    public String useYn;
    public Timestamp createTime;
    public Timestamp updateTime;
}
