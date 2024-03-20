package yj.board.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.Category;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<ArrayList<CategoryDto>> findCategory(@RequestParam int type) {
        return ResponseEntity.ok(categoryService.findCategory(type));
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
