package yj.board.repository.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import yj.board.domain.article.Article;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class JpaArticleRepositoryTest {

    @Autowired
    private JpaArticleRepository articleRepository;

    @Test
    @DisplayName("모든 게시글 조회")
    void findAll() {
        List<Article> articles = articleRepository.findAll();

        assertThat(articles).isNotEmpty();
    }
}
