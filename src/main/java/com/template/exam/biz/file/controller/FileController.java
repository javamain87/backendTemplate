package com.template.exam.biz.file.controller;

import com.template.exam.biz.file.model.FileVO;
import com.template.exam.biz.file.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 단일 파일 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<FileVO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("boardSeq") Integer boardSeq) {

        FileVO fileVO = fileService.uploadFile(file, boardSeq);
        return ResponseEntity.ok(fileVO);
    }

    /**
     * 다중 파일 업로드
     */
    @PostMapping(value= "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileVO>> uploadMultipleFiles(
            @RequestPart("files") MultipartFile[] files,
            @RequestParam("boardSeq") Integer boardSeq) {

        List<MultipartFile> fileList = Arrays.asList(files);
        List<FileVO> uploadedFiles = fileService.uploadFiles(fileList, boardSeq);
        return ResponseEntity.ok(uploadedFiles);
    }

    /**
     * 파일 다운로드
     */
    // 컨트롤러에 새로운 엔드포인트 추가
    @GetMapping("/download/large/{fileId}")
    public void downloadLargeFile(@PathVariable Integer fileId,  HttpServletResponse response) {
        try {
            // 파일 정보 조회
            FileVO fileInfo = fileService.getFileInfo(fileId);
            if (fileInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
                return;
            }

            // 응답 헤더 설정
            response.setContentType(fileInfo.getContentType() != null ?
                    fileInfo.getContentType() : "application/octet-stream");

            String encodedFilename = new String(fileInfo.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");

            // 대용량 파일 스트리밍 처리
            fileService.downloadFile(fileId, response.getOutputStream());

        } catch (IOException e) {
            log.error("파일 다운로드 중 오류 발생: {}", e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류가 발생했습니다.");
            } catch (IOException ex) {
                log.error("오류 응답 전송 실패", ex);
            }
        }
    }

    /**
     * 파일 정보 조회
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<FileVO> getFileInfo(@PathVariable Integer fileId) {
        FileVO fileInfo = fileService.getFileInfo(fileId);
        return ResponseEntity.ok(fileInfo);
    }

    /**
     * 게시글에 연결된 파일 목록 조회
     */
    @GetMapping("/board/{boardSeq}")
    public ResponseEntity<List<FileVO>> getFilesByBoardSeq(@PathVariable Integer boardSeq) {
        List<FileVO> files = fileService.getFilesByBoardSeq(boardSeq);
        return ResponseEntity.ok(files);
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글에 연결된 모든 파일 삭제
     */
    @DeleteMapping("/board/{boardSeq}")
    public ResponseEntity<Void> deleteFilesByBoardSeq(@PathVariable Integer boardSeq) {
        fileService.deleteFilesByBoardSeq(boardSeq);
        return ResponseEntity.ok().build();
    }
}
