package yj.board.controller.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import yj.board.domain.member.dto.AuthorityDto;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.exception.LoginFailException;
import yj.board.service.TokenService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("로그인 성공")
    public void login_success() throws Exception{
        //given
        LoginDto loginDto = getLoginDto();
        TokenDto tokenDto = getTokenDto();

        String object = objectMapper.writeValueAsString(loginDto);
        given(tokenService.login(any(LoginDto.class))).willReturn(tokenDto);

        //when
        ResultActions actions = requestRegisterUrl(object, "/token", "post");

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("RefreshToken"));
    }

    @Test
    @DisplayName("로그인 실패")
    public void login_fail() throws Exception{
        //given
        LoginDto loginDto = getLoginDto();
        TokenDto tokenDto = getTokenDto();

        String object = objectMapper.writeValueAsString(loginDto);
        given(tokenService.login(any(LoginDto.class))).willThrow(new LoginFailException());

        //when
        ResultActions actions = requestRegisterUrl(object, "/token", "post");

        //then
        String errorCode = "$.[?(@.code == '%s')]";
        String errorStatus = "$.[?(@.status == '%s')]";

        actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(errorCode, "CM_004").exists())
                .andExpect(jsonPath(errorStatus, 401).exists());

    }

    @Test
    @DisplayName("토큰 재발급 성공")
    public void reissue_success() throws Exception{
        //given
        TokenDto tokenDto = getTokenDto();
        String newAccessToken = "new";
        String newRefreshToken = "new";
        String object = objectMapper.writeValueAsString(tokenDto);
//        given(tokenService.reissue(any(TokenDto.class))).willReturn(tokenDto = TokenDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());
        given(tokenService.reissue(any(TokenDto.class))).willReturn(ReissueTokenDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).memberDto(getMemberDto()).build());
        //when
        ResultActions actions = requestRegisterUrl(object, "/token", "put");

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("RefreshToken"))
                .andExpect(header().string("Authorization", "Bearer " + newAccessToken))
                .andExpect(header().string("RefreshToken", "Bearer " + newRefreshToken));
    }

    private ResultActions requestRegisterUrl(String object, String url, String method) throws Exception {
        ResultActions actions = mockMvc.perform(post(url)
                .content(object)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        if (method.equals("put")) {
            actions = mockMvc.perform(put(url)
                    .content(object)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        return actions;
    }

    private static LoginDto getLoginDto() {
        LoginDto loginDto = new LoginDto("loginTest", "Testtest1");
        return loginDto;
    }

    private static TokenDto getTokenDto() {
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        return tokenDto;
    }

    private static MemberDto getMemberDto() {
        Set<AuthorityDto> authorities = new HashSet<>();
        authorities.add(new AuthorityDto("ROLE_USER"));
        return MemberDto.builder()
                .loginId("conTest1")
                .password("conTest1")
                .authorityDtoSet(authorities)
                .nickname("conTest1")
                .build();
    }

}
