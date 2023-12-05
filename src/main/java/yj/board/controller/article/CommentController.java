package yj.board.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentUpdateDto;
import yj.board.domain.comment.dto.CommentWriteDto;
import yj.board.service.CommentService;

import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글별 댓글 전체조회
    @GetMapping("")
    public ResponseEntity<ArrayList<CommentDto>> findComment(@RequestParam String atcNum) {
        return ResponseEntity.ok(commentService.findComment(atcNum));
    }

    // 댓글 달기 & 답글 달기
    @PostMapping("")
    public ResponseEntity<ArrayList<CommentDto>> insertComment(@RequestBody CommentWriteDto comment) {

        commentService.insertComment(comment);
        ArrayList<CommentDto> comments = commentService.findComment(comment.getAtcNum());

        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentUpdateDto comment) {
        commentService.updateComment(comment);
        return ResponseEntity.ok(null);
    }

    // 댓글 삭제
    @DeleteMapping("")
    public ResponseEntity<CommentDto> deleteComment(@RequestParam String cmtNum) {
        commentService.deleteComment(cmtNum);
        return ResponseEntity.ok(null);
    }

}
