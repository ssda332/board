package yj.board.domain.board.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDto {
    private Long ctgId;
    private String ctgTitle;
    private Long ctgHierachy;
//    private String ctgRegDate; // 데이터 타입 변경
//    private String ctgUptDate; // 데이터 타입 변경
    private Long ctgSort;
    private Long ctgPrtId;
/*
    public static CategoryDto from(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
                .ctgId(category.getCtgId())
                .ctgTitle(category.getCtgTitle())
                .ctgHierachy(category.getCtgHierachy())
                .ctgRegDate(category.getCtgRegDate().toString())
                .ctgUptDate(category.getCtgUptDate().toString())
                .ctgSort(category.getCtgSort())
                .ctgPrtId(category.getCtgPrtId())
                .build();
    }*/



}
