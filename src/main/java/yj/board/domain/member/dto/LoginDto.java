package yj.board.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "영문자와 숫자만 써주세요")
    private String loginId;

    @NotEmpty
    private String password;
}
