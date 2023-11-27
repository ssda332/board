package yj.board.repository.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentWriteDto;
import yj.board.repository.CommentRepository;
import yj.board.repository.mybatis.mapper.CommentMapper;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class MyBatisCommentRepository implements CommentRepository {

    private final CommentMapper commentMapper;


    @Override
    public ArrayList<CommentDto> findComment(String atcNum) {
        return commentMapper.findComment(atcNum);
    }

    @Override
    public void insertComment(CommentWriteDto comment) {
        commentMapper.insertComment(comment);
    }
}
