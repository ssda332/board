package yj.board.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.article.dto.PageInfo;
import yj.board.domain.article.dto.Pagination;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 카테고리 게시판 이동
    @GetMapping("")
    public ModelAndView boardMv(ModelAndView mv,
                                @RequestParam(value="category", required=false, defaultValue="1") String category,
                                @RequestParam(value="page", required=false, defaultValue="1") Integer page) {

        int currentPage = page != null ? page : 1;
        int listCount = articleService.selectArticleCount(category);
        log.debug("count : {}", listCount);

        PageInfo pi = Pagination.getPageInfo(currentPage, listCount);

        ArrayList<ArticleDto> articleList = articleService.getArticleList(pi, category);
        log.debug("getBoardList = {}", articleList);
        log.debug("pi = {}", pi);

        mv.addObject("list", articleList);
        mv.addObject("pi", pi);
        mv.addObject("category", category);
        mv.setViewName("board/articleList");

        return mv;
    }

    // 게시판 글쓰기 페이지 이동
    @GetMapping("new")
    public String writeArticlePage() {
        return "board/writeArticle";
    }

    // 게시글 작성
    @PostMapping("new")
    public ResponseEntity<ArticleWriteDto> writeArticle(@RequestBody ArticleWriteDto articleDto) {
        log.info("dto : {}", articleDto);
        articleService.writeArticle(articleDto);

        return ResponseEntity.ok(articleDto);
    }

    // 게시글 상세보기 페이지 이동
    @GetMapping("/{atcSeq}")
    public ModelAndView updateArticlePage(ModelAndView mv,
                                          @PathVariable long atcSeq) {
        String atcNum = String.valueOf(atcSeq);
        ArticleDto article = articleService.findArticle(atcNum);
        log.debug("article = {}", article);

        mv.addObject("article", article);
        mv.setViewName("board/articleDetail");
        return mv;
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
