package yj.board.repository.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yj.board.domain.board.Category;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.repository.CategoryRepository;
import yj.board.repository.mybatis.mapper.CategoryMapper;

import java.util.ArrayList;
import java.util.List;

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
}
