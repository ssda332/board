package yj.board.domain.comment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentUpdateDto {
    @NotNull
    private String cmtNum;
    @NotNull
    @Size(max = 200, message = "댓글 길이가 너무 깁니다.")
    private String cmtContent;
    @NotNull
    private String atcNum;
    @NotNull
    private String memId;
}
