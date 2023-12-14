package yj.board.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.comment.dto.CommentDto;
import yj.board.domain.comment.dto.CommentUpdateDto;
import yj.board.domain.comment.dto.CommentWriteDto;
import yj.board.exception.comment.CommentAccessDeniedException;
import yj.board.service.CommentService;

import javax.validation.Valid;
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
    public ResponseEntity<ArrayList<CommentDto>> insertComment(@RequestBody @Valid CommentWriteDto comment) {

        commentService.insertComment(comment);
        ArrayList<CommentDto> comments = commentService.findComment(comment.getAtcNum());

        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("")
    public ResponseEntity<ArrayList<CommentDto>> updateComment(@RequestBody @Valid CommentUpdateDto comment, @AuthenticationPrincipal PrincipalDetails member) {
        checkAuth(comment.getMemId(), member);

        commentService.updateComment(comment);
        ArrayList<CommentDto> commentList = commentService.findComment(comment.getAtcNum());

        return ResponseEntity.ok(commentList);
    }

    // 댓글 삭제
    @DeleteMapping("")
    public ResponseEntity<ArrayList<CommentDto>> deleteComment(@RequestBody CommentUpdateDto comment, @AuthenticationPrincipal PrincipalDetails member) {
        checkAuth(comment.getMemId(), member);

        commentService.deleteComment(comment.getCmtNum());
        ArrayList<CommentDto> commentList = commentService.findComment(comment.getAtcNum());
        return ResponseEntity.ok(commentList);
    }

    private static void checkAuth(String commentMemId, PrincipalDetails member) {
        String id = member.getMember().getId() + "";
        if (!commentMemId.equals(id)) {
            throw new CommentAccessDeniedException();
        }
    }

}
