package yj.board.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.board.dto.ArticleDto;
import yj.board.service.BoardService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("{seq}")
@RequiredArgsConstructor
public class ArticleController {

    private final BoardService boardService;

    // 카테고리 게시판 이동
    @GetMapping("")
    public String myPage(@PathVariable long seq) {
        return "board/articleList";
    }

    // 게시판 글쓰기 페이지 이동
    @GetMapping("/article")
    public String writeArticlePage(@PathVariable long seq) {
        return "members/writeArticle";
    }

    // 카테고리별 게시글 조회
    @PostMapping("/list")
    public ResponseEntity<List<ArticleDto>> findList(@PathVariable long seq) {
        return null;
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/article/{atcSeq}")
    public String writeArticlePage(@PathVariable long seq, @PathVariable long atcSeq) {
        return "members/writeArticle";
    }

    // 게시글 작성
    @PostMapping("/article")
    public String writeArticle(@PathVariable long seq) {
        return "members/writeArticle";
    }

    // 게시글 수정
    @PutMapping("/article/{atcSeq}")
    public String updateArticle(@PathVariable long seq, @PathVariable long atcSeq) {
        return "members/writeArticle";
    }

    // 게시글 삭제
    @DeleteMapping("/article/{atcSeq}")
    public String deleteArticle(@PathVariable long seq, @PathVariable long atcSeq) {
        return "members/writeArticle";
    }


/*    @PostMapping("/{seq}")
    public ResponseEntity<MemberDto> signup() {
        return null;
    }*/



}
