package yj.board.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import yj.board.domain.board.dto.CategoryDto;
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

    // 모든 카테고리 조회
    @GetMapping("list")
    public ResponseEntity<ArrayList<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
