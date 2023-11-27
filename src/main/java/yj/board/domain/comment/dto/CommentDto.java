package yj.board.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {
    /*CMT_NUM
    CMT_CONTENT
    CMT_HIERACHY
    CMT_ACTIVATED
    CMT_REG_DATE
    CMT_UPT_DATE
    ATC_NUM
    CMT_PRT_NUM
    MEM_ID*/
    private String cmtNum;
    private String cmtContent;
    private long cmtHierachy;
    private long cmtActivated;
    private String cmtRegDate;
    private String cmtUptDate;
    private String atcNum;
    private String cmtPrtNum;
    private String memId;
    private String memNickname;
}
