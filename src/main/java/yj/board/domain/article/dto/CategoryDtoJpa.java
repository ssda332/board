package yj.board.domain.article.dto;

import lombok.*;
import yj.board.domain.article.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDtoJpa {
    private String ctgId;
    private String ctgTitle;
    private Long ctgSort;
    private Long ctgHierachy;
    private Long ctgActivated;
    private ParentCategory parent;
    private List<CategoryDtoJpa> child;

    public static CategoryDtoJpa from(Category category) {
        if (category == null) return null;

        return CategoryDtoJpa.builder()
                .ctgId(Long.toString(category.getCtgId()))
                .ctgTitle(category.getCtgTitle())
                .ctgSort(category.getCtgSort())
                .ctgHierachy(category.getParents() != null ? category.getParents().getCtgHierachy() + 1 : 1)
                .ctgActivated(category.getCtgActivated())
                .parent(category.getParents() != null ? ParentCategory.from(category.getParents()) : null) // 부모 ID 설정
                .child(category.getChild() != null ? category.getChild().stream()
                        .map(CategoryDtoJpa::from) // 재귀적 호출을 통한 자식 DTO 변환
                        .collect(Collectors.toList()) : null)
                .build();
    }

}
