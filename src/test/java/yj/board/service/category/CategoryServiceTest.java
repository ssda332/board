package yj.board.service.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.repository.CategoryRepository;
import yj.board.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Spy
    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    private Long ctgSeq = 0L;

    @Test
    @DisplayName("변경된 카테고리 내용 저장")
    void saveCategory() {
        // 테스트 데이터 준비
        ArrayList<CategoryEditDto> data = new ArrayList<>();
        CategoryEditDto newCategory3 = getCategoryEditDto("c", "new_3", "카테고리3");
        CategoryEditDto newCategory4 = getCategoryEditDto("c", "new_4", "카테고리4");
        CategoryEditDto newCategory5 = getCategoryEditDto("c", "new_5", "카테고리5");
        CategoryEditDto editCategory1 = getCategoryEditDto("u", "new_1", "카테고리1-1");
        CategoryEditDto deleteCategory2 = getCategoryEditDto("d", "new_2", "카테고리2");

        data.add(newCategory3);
        data.add(newCategory4);
        data.add(newCategory5);
        data.add(editCategory1);
        data.add(deleteCategory2);

        given(categoryRepository.findAll_edit()).willReturn(data);
        given(categoryRepository.selectNewCtgId()).willReturn(1L);
        doNothing().when(categoryRepository).insertCategory(any(CategoryEditDto.class));
        doNothing().when(categoryRepository).updateCategory(any(CategoryEditDto.class));
        doNothing().when(categoryRepository).deleteCategory(any(CategoryEditDto.class));

        // 메소드 실행
        List<CategoryEditDto> result = categoryService.saveCategory(data);

        verify(categoryRepository, times(3)).selectNewCtgId();
        verify(categoryRepository, times(3)).insertCategory(any(CategoryEditDto.class));
        verify(categoryRepository, times(1)).updateCategory(any(CategoryEditDto.class));
        verify(categoryRepository, times(1)).deleteCategory(any(CategoryEditDto.class));
    }

    private static CategoryEditDto getCategoryEditDto(String status, String ctgId, String ctgTitle) {
        return CategoryEditDto.builder()
                .status(status)
                .ctgId(ctgId)
                .ctgTitle(ctgTitle)
                .build();
    }

}
