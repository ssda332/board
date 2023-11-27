package yj.board.repository.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yj.board.domain.article.Category;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.repository.CategoryRepository;
import yj.board.repository.mybatis.mapper.CategoryMapper;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class MyBatisCategoryRepository implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public ArrayList<CategoryDto> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public ArrayList<CategoryDto> findCanWrite() {
        return categoryMapper.findCanWrite();
    }

    @Override
    public ArrayList<CategoryEditDto> findAll_edit() {
        return categoryMapper.findAll_edit();
    }

    @Override
    public long selectNewCtgId() {
        return categoryMapper.selectNewCtgId();
    }

    @Override
    public void insertCategory(CategoryEditDto category) {
        categoryMapper.insertCategory(category);
    }

    @Override
    public void updateCategory(CategoryEditDto category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategory(CategoryEditDto category) {
        categoryMapper.deleteCategory(category);
    }
}
