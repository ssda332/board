package yj.board.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.domain.board.dto.CategoryEditDto;
import yj.board.service.CategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    // 카테고리 편집창 이동
    @GetMapping("")
    public String editCategoryPage() {
        return "board/editCategory";
    }

    // 사이드바 모든 카테고리 조회
    @GetMapping("list")
    public ResponseEntity<ArrayList<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // 카테고리 편집창에서 모든 카테고리 조회
    @PostMapping("list")
    public ResponseEntity<ArrayList<CategoryEditDto>> findEditedAll() {
        return ResponseEntity.ok(categoryService.findAll_edit());
    }

    // 카테고리 편집내용 저장
    @PutMapping("list")
    public ResponseEntity<ArrayList<CategoryEditDto>> saveCategory(@RequestBody List<CategoryEditDto> data) {
        return ResponseEntity.ok(categoryService.saveCategory(data));
    }

}
