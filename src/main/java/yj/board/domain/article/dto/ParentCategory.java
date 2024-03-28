package yj.board.domain.article.dto;

import lombok.*;
import yj.board.domain.article.Category;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParentCategory {
    private String ctgId;
    private String ctgTitle;
    private Long ctgHierachy;

    public static ParentCategory from(Category category) {
        if (category == null) return null;

        return ParentCategory.builder()
                .ctgId(Long.toString(category.getCtgId()))
                .ctgTitle(category.getCtgTitle())
                .ctgHierachy(category.getCtgHierachy())
                .build();

    }
}
