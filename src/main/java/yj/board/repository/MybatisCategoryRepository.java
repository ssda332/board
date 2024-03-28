package yj.board.repository;

import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.dto.CategoryEditDto;

import java.util.ArrayList;

public interface MybatisCategoryRepository {
    ArrayList<CategoryDto> findAllCategories();
    ArrayList<CategoryDto> findCanWrite();
    ArrayList<CategoryEditDto> findAll_edit();

    long selectNewCtgId();
    void insertCategory(CategoryEditDto category);
    void updateCategory(CategoryEditDto category);
    void deleteCategory(CategoryEditDto category);
}
