package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    @Qualifier("myBatisCategoryRepository")
    private final CategoryRepository categoryRepository;

    public ArrayList<CategoryDto> findAll() {
        return categoryRepository.findAll();
    }



}
