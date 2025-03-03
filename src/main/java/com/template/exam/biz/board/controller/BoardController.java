package com.template.exam.biz.board.controller;

import com.template.exam.biz.board.model.ReqBoardVO;
import com.template.exam.biz.board.model.ResBoardVO;
import com.template.exam.biz.board.model.ResListVO;
import com.template.exam.biz.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/insert")
    public ResponseEntity<String> boardSave(@RequestBody ReqBoardVO reqBoardVO) {
        boardService.saveBoard(reqBoardVO);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResBoardVO> boardDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PostMapping("/boardList")
    public ResponseEntity<ResListVO> boardList(@RequestBody ReqBoardVO reqVO) {
        return ResponseEntity.ok(boardService.getBoardList(reqVO));
    }

    @PostMapping("/modify")
    public ResponseEntity<String> boardModify(@RequestBody ReqBoardVO boardVO) {
        boardService.updateBoard(boardVO);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> boardRemove(@RequestBody ReqBoardVO boardVO) {
        boardService.deleteBoard(boardVO.getSeq());
        return ResponseEntity.ok("success");
    }
}
