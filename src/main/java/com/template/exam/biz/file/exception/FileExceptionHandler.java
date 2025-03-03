package com.template.exam.biz.file.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class FileExceptionHandler {

    /**
     * 최대 업로드 크기 초과 예외 처리
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.error("파일 크기 제한 초과: {}", exc.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "파일 크기가 제한을 초과했습니다. 최대 10MB까지 업로드할 수 있습니다.");
        response.put("error", "Max Upload Size Exceeded");

        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    /**
     * 파일 업로드/다운로드 관련 예외 처리
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException exc) {
        log.error("파일 처리 중 오류 발생: {}", exc.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "파일 처리 중 오류가 발생했습니다.");
        response.put("error", exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 일반 런타임 예외 처리
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException exc) {
        log.error("런타임 오류 발생: {}", exc.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "요청 처리 중 오류가 발생했습니다.");
        response.put("error", exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
