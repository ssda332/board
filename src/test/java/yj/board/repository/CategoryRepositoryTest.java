package yj.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.repository.mybatis.mapper.CategoryMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("모든 카테고리 가져오기")
    void findAll() {
        List<CategoryDto> all = categoryMapper.findAll();

        all.stream().forEach(s -> {
            System.out.println(s.toString());
        });
        assertThat(all).isNotNull();
//        assertThat()

    }
}
