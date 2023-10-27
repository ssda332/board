package yj.board.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import yj.board.domain.member.Member;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotNull
//    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "ID는 숫자와 영문 대,소문자 조합으로 4자 이상 12자 이하입니다.")
    private String loginId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
//    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[A-Za-z0-9]{8,12}$", message = "비밀번호는 영문 대,소문자와 숫자 8자 이상 12자 이하입니다.")
    private String password;

    @NotNull
    @Size(min = 3, max = 8)
    private String nickname;

    private Set<AuthorityDto> authorityDtoSet;

    public static MemberDto from(Member member) {
        if (member == null) return null;

        return MemberDto.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .authorityDtoSet(member.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }

}