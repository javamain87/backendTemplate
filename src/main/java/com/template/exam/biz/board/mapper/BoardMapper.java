package com.template.exam.biz.board.mapper;

import com.template.exam.biz.board.model.BoardVO;
import com.template.exam.biz.board.model.ReqBoardVO;
import com.template.exam.biz.board.model.ResBoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<ResBoardVO> getBoardList(ReqBoardVO reqVO);
    int getBoardCnt(ReqBoardVO reqVO);
    ResBoardVO getBoardById(Integer id);
    void saveBoard(ReqBoardVO boardVO);
    ResBoardVO updateBoard(ReqBoardVO boardVO);
    ResBoardVO deleteBoard(Integer id);
}
