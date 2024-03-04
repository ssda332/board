package yj.board.repository.mybatis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.repository.mybatis.mapper.ArticleMapper;
import yj.board.repository.mybatis.mapper.CategoryMapper;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
//@TestPropertySource(locations = "classpath:application-test.yml")
public class ArticleRepositoryTest {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    private CategoryEditDto category1;

    @BeforeEach
    void beforeEach() {
        category1 = CategoryEditDto.builder()
                .ctgId("test1").ctgTitle("selectTest1").ctgHierachy(1L).ctgSort(1L).ctgPrtId("")
                .build();

        categoryMapper.insertCategory(category1);
    }

    @Test
    @DisplayName("게시글 추가")
    void insertArticle() {
        // given
/*        ArticleWriteDto.builder()
                .atcNum("test1")
                .atcTitle("테스트제목1")
                .atcContent("테스트내용1")
                .atcWriter()
                .ctgId(category1.getCtgId())*/
        // when
        // then
    }

}
