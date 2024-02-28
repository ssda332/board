package yj.board.repository.mybatis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.repository.mybatis.mapper.CategoryMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
//@TestPropertySource(locations = "classpath:application-test.yml")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryMapper categoryMapper;

    @BeforeEach
    void beforeEach() {
        CategoryEditDto category1 = CategoryEditDto.builder()
                .ctgId("test1").ctgTitle("selectTest1").ctgHierachy(1L).ctgSort(1L).ctgPrtId("")
                .build();

        CategoryEditDto category2 = CategoryEditDto.builder()
                .ctgId("test2").ctgTitle("selectTest2").ctgHierachy(1L).ctgSort(2L).ctgPrtId("")
                .build();

        CategoryEditDto category3 = CategoryEditDto.builder()
                .ctgId("test3").ctgTitle("selectTest3").ctgHierachy(1L).ctgSort(3L).ctgPrtId("")
                .build();

        categoryMapper.insertCategory(category1);
        categoryMapper.insertCategory(category2);
        categoryMapper.insertCategory(category3);
    }

    @Test
    @DisplayName("모든 카테고리 가져오기")
    void findAll() {
        List<CategoryDto> all = categoryMapper.findAll();
        findTest(all, "test1");
        findTest(all, "test2");
        findTest(all, "test3");

    }

    @Test
    @DisplayName("모든 카테고리 편집목록 가져오기")
    void findAll_edit() {
        ArrayList<CategoryEditDto> all = categoryMapper.findAll_edit();
        findTest(all, "test1");
        findTest(all, "test2");
        findTest(all, "test3");

    }

    @Test
    @DisplayName("카테고리 저장 - 새 카테고리 추가")
    void insertCategory() {
        CategoryEditDto category4 = CategoryEditDto.builder()
                .ctgId("test4").ctgTitle("selectTest4").ctgHierachy(1L).ctgSort(1L).ctgPrtId("")
                .build();

        categoryMapper.insertCategory(category4);
        ArrayList<CategoryEditDto> all = categoryMapper.findAll_edit();
        findTest(all, "test4");
    }

    @Test
    @DisplayName("카테고리 저장 - 기존 카테고리 변경")
    void updateCategory() {
        String changeTitle = "changeTitle1";
        CategoryEditDto category4 = CategoryEditDto.builder()
                .ctgId("test1").ctgTitle(changeTitle).ctgHierachy(1L).ctgSort(1L).ctgPrtId("").ctgSort(3L)
                .build();

        categoryMapper.updateCategory(category4);
        ArrayList<CategoryEditDto> all = categoryMapper.findAll_edit();
        assertThat(all)
                .extracting(CategoryEditDto::getCtgTitle)
                .contains(changeTitle);
    }

    @Test
    @DisplayName("카테고리 저장 - 기존 카테고리 제거")
    void deleteCategory() {
        ArrayList<CategoryEditDto> all = categoryMapper.findAll_edit();
        all.forEach(category -> {
            categoryMapper.deleteCategory(category);
        });

        all = categoryMapper.findAll_edit();

        // 크기가 0인지 확인
        assertThat(all.size()).isEqualTo(0);
    }

    private static void findTest(List<CategoryDto> all, String target) {
        // "test" ctgId가 목록에 있는지 확인
        assertThat(all)
                .extracting(CategoryDto::getCtgId)
                .contains(target);
    }

    private static void findTest(ArrayList<CategoryEditDto> all, String target) {
        // "test" ctgId가 목록에 있는지 확인
        assertThat(all)
                .extracting(CategoryEditDto::getCtgId)
                .contains(target);
    }
}
