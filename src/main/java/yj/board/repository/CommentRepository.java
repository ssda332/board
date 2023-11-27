package yj.board.repository;

import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentWriteDto;

import java.util.ArrayList;

public interface CommentRepository {
    ArrayList<CommentDto> findComment(String atcNum);
    void insertComment(CommentWriteDto comment);
}
