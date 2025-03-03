package com.template.exam.biz.board.service;

import com.template.exam.biz.board.mapper.BoardMapper;
import com.template.exam.biz.board.model.BoardVO;
import com.template.exam.biz.board.model.ReqBoardVO;
import com.template.exam.biz.board.model.ResBoardVO;
import com.template.exam.biz.board.model.ResListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public void saveBoard(ReqBoardVO boardVO) {
        boardMapper.saveBoard(boardVO);
    }

    public ResBoardVO updateBoard(ReqBoardVO boardVO) {
        return boardMapper.updateBoard(boardVO);
    }

    public ResBoardVO deleteBoard(int seq) {
       return boardMapper.deleteBoard(seq);
    }

    public ResBoardVO getBoardById(Integer seq) {
        return boardMapper.getBoardById(seq);
    }

    public ResListVO getBoardList(ReqBoardVO reqVO) {
        ResListVO res = new ResListVO();
        res.setList(boardMapper.getBoardList(reqVO));
        res.setTotalCnt(boardMapper.getBoardCnt(reqVO));
        return res;
    }
}
