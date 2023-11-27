package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.dto.CategoryEditDto;

import java.util.ArrayList;

@Mapper
public interface CategoryMapper {

    ArrayList<CategoryDto> findAll();
    ArrayList<CategoryDto> findCanWrite();
    ArrayList<CategoryEditDto> findAll_edit();
    void insertCategory(CategoryEditDto categoryEditDto);
    void updateCategory(CategoryEditDto categoryEditDto);
    void deleteCategory(CategoryEditDto categoryEditDto);
    long selectNewCtgId();
}
