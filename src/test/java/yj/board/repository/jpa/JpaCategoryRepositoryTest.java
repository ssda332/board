package yj.board.repository.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import yj.board.domain.article.Category;
import yj.board.repository.CategoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class JpaCategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("계층별 카테고리 조회")
    void 계층별_카테고리_조회() {
        List<Category> categories = categoryRepository.findTopCategories();

        assertThat(categories).isNotEmpty();
    }

    @Test
    void findByParentId() {
        List<Category> categories = categoryRepository.findTopCategories();

    }
}