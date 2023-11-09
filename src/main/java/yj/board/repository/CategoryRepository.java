package yj.board.repository;

import yj.board.domain.board.Category;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.domain.board.dto.CategoryEditDto;

import java.util.ArrayList;
import java.util.List;

public interface CategoryRepository {

    Category save(Category category);
    ArrayList<CategoryDto> findAll();
    ArrayList<CategoryEditDto> findAll_edit();

    long selectNewCtgId();
    void insertCategory(CategoryEditDto category);
    void updateCategory(CategoryEditDto category);
    void deleteCategory(CategoryEditDto category);
}
