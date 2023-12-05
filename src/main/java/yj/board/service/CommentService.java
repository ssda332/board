package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentUpdateDto;
import yj.board.domain.comment.dto.CommentWriteDto;
import yj.board.repository.ArticleRepository;
import yj.board.repository.CommentRepository;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    @Qualifier("myBatisCommentRepository")
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public ArrayList<CommentDto> findComment(String atcNum) {
        return commentRepository.findComment(atcNum);
    }

    public void insertComment(CommentWriteDto comment) {
        commentRepository.insertComment(comment);
    }

    public void deleteComment(String cmtNum) {
        commentRepository.deleteComment(cmtNum);
    }

    public void updateComment(CommentUpdateDto comment) {
        commentRepository.updateComment(comment);
    }
}
