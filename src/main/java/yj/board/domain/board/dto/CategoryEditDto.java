package yj.board.domain.board.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryEditDto {

    private String ctgId;
    private String ctgPrtId;
    private String ctgTitle;
    private String ctgPrtTitle;
    //    private String ctgRegDate; // 데이터 타입 변경
//    private String ctgUptDate; // 데이터 타입 변경
    private Long ctgHierachy;
    private Long ctgSort;
    private String status;



}
