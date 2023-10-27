package yj.board.domain.member.dto;

import lombok.*;
import yj.board.domain.member.Member;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {

    private Long id;
    private String loginId;

    public static MemberInfoDto from(Member member) {
        if (member == null) return null;

        return MemberInfoDto.builder()
                .loginId(member.getLoginId())
                .id(member.getId())
                .build();
    }
}
