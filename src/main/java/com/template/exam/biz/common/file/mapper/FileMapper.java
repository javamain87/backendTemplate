package com.template.exam.biz.common.file.mapper;

import com.template.exam.biz.common.file.model.FileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    // 파일 저장
    void saveFile(FileVO fileVO);

    // 다중 파일 저장
    void saveFiles(List<FileVO> files);

    // 파일 정보 조회
    FileVO getFileById(Integer fileId);

    // 게시글에 연결된 모든 파일 조회
    List<FileVO> getFilesByBoardSeq(Integer boardSeq);

    // 파일 정보 수정
    void updateFile(FileVO fileVO);

    // 파일 삭제
    void deleteFile(Integer fileId);

    // 게시글에 연결된 모든 파일 삭제
    void deleteFilesByBoardSeq(Integer boardSeq);
}
