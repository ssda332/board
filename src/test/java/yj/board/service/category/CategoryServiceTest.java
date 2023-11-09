package yj.board.service.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yj.board.repository.CategoryRepository;
import yj.board.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("레이아웃 카테고리 조회 성공")
    void loginSuccess() {

    }

}
