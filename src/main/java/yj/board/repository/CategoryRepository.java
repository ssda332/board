package yj.board.repository;

import yj.board.domain.board.Category;
import yj.board.domain.board.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public interface CategoryRepository {

    Category save(Category category);
    ArrayList<CategoryDto> findAll();
}
