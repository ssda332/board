package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.board.domain.board.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CategoryMapper {

    ArrayList<CategoryDto> findAll();

}
