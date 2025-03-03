package com.template.exam.biz.file.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileVO {
    private Integer fileId;
    private String originalFilename;
    private String savedFilename;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private Integer boardSeq;
    private Timestamp uploadTime;
}
