package yj.board.domain.member.dto;

import lombok.*;
import yj.board.domain.member.Member;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    @NotNull
    @Size(min = 3, max = 8)
    private String nickname;

    public static MemberUpdateDto fromUpdate(Member member) {
        if (member == null) return null;

        return MemberUpdateDto.builder()
                .nickname(member.getNickname())
                .build();
    }
}
