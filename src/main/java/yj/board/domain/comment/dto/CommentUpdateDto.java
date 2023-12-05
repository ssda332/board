package yj.board.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentUpdateDto {
    private String cmtNum;
    private String cmtContent;
}
