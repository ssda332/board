package yj.board.domain.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageInfo {
    private int currentPage;
    private int listCount;
    private int pageLimit;
    private int maxPage;
    private int startPage;
    private int endPage;
    private int boardLimit;

}
