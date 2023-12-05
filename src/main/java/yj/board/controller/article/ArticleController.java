package yj.board.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import yj.board.domain.article.dto.*;
import yj.board.domain.file.FileDto;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.jwt.JwtProperties;
import yj.board.jwt.TokenProvider;
import yj.board.service.ArticleService;
import yj.board.service.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final S3Uploader s3Uploader;

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
    @GetMapping("form")
    public ModelAndView writeArticlePage(ModelAndView mv, @RequestParam("atcNum") String atcNum) {
        if (!atcNum.equals("new")) {
            // 게시글 수정 요청일때
            ArticleDetailDto article = articleService.findArticle(atcNum, false);
            mv.addObject("article", article);
        } else {
            // 새 게시글 작성 요청일때
            ArticleDto build = ArticleDto.builder()
                    .atcContent("")
                    .atcTitle("")
                    .build();
            mv.addObject("article", build);

        }

        mv.setViewName("board/writeArticle");
        return mv;
    }

    private void checkWriterAuthority(HttpServletRequest request, String atcNum) {
        String bearerToken = request.getHeader(JwtProperties.HEADER_STRING);
        articleService.checkMember(atcNum, bearerToken);
    }

    // 게시글 작성
    @Transactional
    @PostMapping("")
    public ResponseEntity<ArticleWriteDto> writeArticle(@RequestBody ArticleWriteDto articleDto) {
        articleService.writeArticle(articleDto);
        s3Uploader.copyFile(articleDto);

        return ResponseEntity.ok(articleDto);
    }

    // 게시글 상세보기 페이지 이동
    @GetMapping("/{atcSeq}")
    public ModelAndView updateArticlePage(ModelAndView mv,
                                          @PathVariable long atcSeq) {
        String atcNum = String.valueOf(atcSeq);
        ArticleDetailDto article = articleService.findArticle(atcNum, true);
        log.debug("article = {}", article);

        mv.addObject("article", article);
        mv.setViewName("board/articleDetail");
        return mv;
    }

    // 게시글 수정 권한 체크
    @PutMapping("/{atcNum}")
    public ResponseEntity<String> updateArticle(HttpServletRequest request, @PathVariable String atcNum) {
        checkWriterAuthority(request, atcNum);

        return ResponseEntity.ok("success");
    }

    // 게시글 삭제
    @DeleteMapping("/{atcNum}")
    public ResponseEntity<String> deleteArticle(HttpServletRequest request, @PathVariable String atcNum) {
        checkWriterAuthority(request, atcNum);

        // 게시글에 업로드된 이미지 검색
        ArrayList<FileDto> files = s3Uploader.findFile(atcNum);

        // 이미지 삭제
        s3Uploader.deleteFile(files, atcNum);

        // 게시글 삭제, 댓글 삭제
        articleService.deleteArticle(atcNum);
        return ResponseEntity.ok("success");
    }

/*    @PostMapping("/{seq}")
    public ResponseEntity<MemberDto> signup() {
        return null;
    }*/



}
