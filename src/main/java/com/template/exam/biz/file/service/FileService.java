package com.template.exam.biz.file.service;

import com.template.exam.biz.file.mapper.FileMapper;
import com.template.exam.biz.file.model.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 단일 파일 업로드
     */
    @Transactional
    public FileVO uploadFile(MultipartFile file, Integer boardSeq) {
        try {
            // 저장 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // 파일명 생성
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path targetLocation = uploadPath.resolve(savedFilename);

            // NIO 채널과 버퍼를 사용한 파일 복사
            try (InputStream inputStream = file.getInputStream();
                 FileChannel fileChannel = FileChannel.open(targetLocation,
                         StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

                // 다이렉트 버퍼 사용으로 성능 향상
                ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
                ReadableByteChannel inputChannel = Channels.newChannel(inputStream);

                while (inputChannel.read(buffer) != -1) {
                    buffer.flip();
                    fileChannel.write(buffer);
                    buffer.clear();
                }
            }

            // 파일 정보 저장
            FileVO fileVO = new FileVO();
            fileVO.setOriginalFilename(originalFilename);
            fileVO.setSavedFilename(savedFilename);
            fileVO.setFilePath(targetLocation.toString());
            fileVO.setFileSize(Files.size(targetLocation));
            fileVO.setContentType(file.getContentType());
            fileVO.setBoardSeq(boardSeq);

            fileMapper.saveFile(fileVO);
            return fileVO;
        } catch (IOException ex) {
            log.error("파일 업로드 실패: {}", ex.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.", ex);
        }
    }

    /**
     * 다중 파일 업로드
     */
    @Transactional
    public List<FileVO> uploadFiles(List<MultipartFile> files, Integer boardSeq) {
        List<FileVO> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                uploadedFiles.add(this.uploadFile(file, boardSeq));
            }
        }

        return uploadedFiles;
    }

    /**
     * 파일 다운로드
     */
    public void downloadFile(Integer fileId, OutputStream outputStream) {
        try {
            FileVO fileVO = fileMapper.getFileById(fileId);
            // 응답 헤더 설정
            if (fileVO == null) {
                throw new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId);
            }

            Path filePath = Paths.get(fileVO.getFilePath());

            // 메모리 매핑 방식으로 대용량 파일 효율적 처리
            try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
                 WritableByteChannel outputChannel = Channels.newChannel(outputStream)) {

                // 대용량 파일은 청크 단위로 처리
                long fileSize = fileChannel.size();
                long position = 0;
                long chunkSize = 8 * 1024 * 1024; // 8MB 청크

                while (position < fileSize) {
                    position += fileChannel.transferTo(position, chunkSize, outputChannel);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("파일 다운로드 중 오류 발생", ex);
        }
    }

    /**
     * 파일 정보 조회
     */
    public FileVO getFileInfo(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    /**
     * 게시글에 연결된 모든 파일 조회
     */
    public List<FileVO> getFilesByBoardSeq(Integer boardSeq) {
        return fileMapper.getFilesByBoardSeq(boardSeq);
    }

    /**
     * 파일 삭제
     */
    @Transactional
    public void deleteFile(Integer fileId) {
        FileVO fileVO = fileMapper.getFileById(fileId);
        if (fileVO != null) {
            // DB에서 삭제
            fileMapper.deleteFile(fileId);

            // 실제 파일 삭제
            try {
                Path filePath = Paths.get(fileVO.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                log.error("파일 삭제 실패: {}", ex.getMessage());
                // 물리적 파일 삭제 실패해도 DB 레코드는 삭제됨
            }
        }
    }

    /**
     * 게시글에 연결된 모든 파일 삭제
     */
    @Transactional
    public void deleteFilesByBoardSeq(Integer boardSeq) {
        List<FileVO> files = fileMapper.getFilesByBoardSeq(boardSeq);

        // DB에서 삭제
        fileMapper.deleteFilesByBoardSeq(boardSeq);

        // 실제 파일 삭제
        for (FileVO fileVO : files) {
            try {
                Path filePath = Paths.get(fileVO.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                log.error("파일 삭제 실패: {}", ex.getMessage());
            }
        }
    }

    /**
     * 파일 정보 업데이트
     */
    @Transactional
    public void updateFile(FileVO fileVO) {
        fileMapper.updateFile(fileVO);
    }
}
