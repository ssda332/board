package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.domain.board.dto.CategoryEditDto;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CategoryMapper {

    ArrayList<CategoryDto> findAll();
    ArrayList<CategoryEditDto> findAll_edit();
    void insertCategory(CategoryEditDto categoryEditDto);
    void updateCategory(CategoryEditDto categoryEditDto);
    void deleteCategory(CategoryEditDto categoryEditDto);
    long selectNewCtgId();
}
