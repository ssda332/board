package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentUpdateDto;
import yj.board.domain.comment.dto.CommentWriteDto;

import java.util.ArrayList;

@Mapper
public interface CommentMapper {

    ArrayList<CommentDto> findComment(String atcNum);
    void insertComment(CommentWriteDto comment);
    void deleteCommentByArticle(String atcNum);
    void deleteComment(String cmtNum);
    void updateComment(CommentUpdateDto comment);
}
