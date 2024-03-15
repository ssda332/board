package yj.board.service.article;

import org.apache.ibatis.session.RowBounds;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.domain.article.dto.PageInfo;
import yj.board.domain.search.Search;
import yj.board.jwt.TokenProvider;
import yj.board.repository.ArticleRepository;
import yj.board.repository.CommentRepository;
import yj.board.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("해당 페이지의 게시글 리스트 가져오기")
    void getArticleList() {
        // Given
        PageInfo pi = new PageInfo();
        String category = "Test Category";
        Search search = new Search();
        ArrayList<ArticleDto> expectedArticles = new ArrayList<>();

        given(articleRepository.selectArticleList(any(RowBounds.class), eq(category), eq(search))).willReturn(expectedArticles);

        // When
        List<ArticleDto> articles = articleService.getArticleList(pi, category, search);

        // Then
        assertThat(articles).isEqualTo(expectedArticles);
    }

    @Test
    @DisplayName("특정 게시물 상세정보 가져오기")
    void findArticleWithMarkdownContent() {
        // Given
        ArticleDetailDto mockArticle = ArticleDetailDto.builder()
                .atcContent("#content")
                .build();
        willDoNothing().given(articleRepository).updateViews(any(String.class));
        given(articleRepository.findArticle(any())).willReturn(mockArticle);

        // When
        ArticleDetailDto article = articleService.findArticle("1", false, false);
        ArticleDetailDto htmlArticle = articleService.findArticle("1", true, false);

        // Then
        assertThat(article.getAtcContent()).isEqualTo(mockArticle.getAtcContent());
        assertThat(htmlArticle.getAtcContent()).isEqualTo(mockArticle.getAtcContent());
    }

}
