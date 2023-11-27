package yj.board.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentWriteDto {
    private String memId;
    private String cmtContent;
    private String atcNum;
    private String cmtPrtNum;
    private Long cmtHierachy;
}
