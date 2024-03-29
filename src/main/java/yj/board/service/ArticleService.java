package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.article.dto.PageInfo;
import yj.board.domain.search.Search;
import yj.board.exception.article.ArticleAccessDeniedException;
import yj.board.jwt.TokenProvider;
import yj.board.repository.ArticleRepository;
import yj.board.repository.CommentRepository;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Qualifier("myBatisArticleRepository")
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    @Value("${cloud.aws.s3.s3url-temp}")
    private String tempS3Url;
    @Value("${cloud.aws.s3.s3url}")
    private String s3Url;

    @Transactional(readOnly = true)
    public ArrayList<ArticleDto> findAllArticle() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ArrayList<ArticleDto> findArticlesByCategory(String seq) {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Integer selectArticleCount(String seq, Search search) {
        return articleRepository.selectArticleCount(seq, search);
    }

    @Transactional(readOnly = true)
    public Integer selectMemberArticleCount(Long id) {
        return articleRepository.selectMemberArticleCount(id);
    }

    public ArrayList<ArticleDto> getArticleList(PageInfo pi, String Category, Search search) {
        // 몇 개의 게시글을 건너 뛸 것인지
        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

        return articleRepository.selectArticleList(rowBounds, Category, search);
    }

    public void writeArticle(ArticleWriteDto articleDto) {
        // temp url 바꿔주기
        String atcContent = articleDto.getAtcContent();
        articleDto.setAtcContent(atcContent.replaceAll(tempS3Url, s3Url));

        // 게시글 등록 & 수정
        if (articleDto.getAtcNum().equals("new")) {
            // auto increment
            String newAtcId = String.valueOf(articleRepository.selectNewAtcNum());
            articleDto.setAtcNum(newAtcId);

            articleRepository.writeArticle(articleDto);
        } else {
            articleRepository.updateArticle(articleDto);
        }

    }

    @Transactional
    public ArticleDetailDto findArticle(String atcNum, boolean markToHtml, boolean hasArticleViewCookie) {
        // 조회수 UPDATE
        if (!hasArticleViewCookie) {
            articleRepository.updateViews(atcNum);
        }

        ArticleDetailDto article = articleRepository.findArticle(atcNum);

        // 수정에 필요한 content는 false(마크다운 형식의 content), 상세보기에 필요한 content는 true(html 형식의 content)
        if (markToHtml) {
            article.setAtcContent(markToHtml(article.getAtcContent()));
        }

        String atcContent = article.getAtcContent();
        String s = atcContent.replaceAll("`", "\\\\`");
        article.setAtcContent(s);
        // ` escape 처리

        return article;
    }

    // 마크다운 형식을 HTML 형식으로 바꿔주는 메소드
    public String markToHtml(String atcContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(atcContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String render = renderer.render(document);

        return render;
    }

    public void deleteArticle(String atcNum) {
        articleRepository.deleteArticle(atcNum);
        commentRepository.deleteCommentByArticle(atcNum);
    }

    public void checkMember(String atcNum, String bearerToken) {
        String memberPk = tokenProvider.getMemberPk(bearerToken);
        String memId = articleRepository.findArticle(atcNum).getMemId();

        if (!memberPk.equals(memId)) throw new ArticleAccessDeniedException("글쓴이가 아닙니다!!!");
    }
}
