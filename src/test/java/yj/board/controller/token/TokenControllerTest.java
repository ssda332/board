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
import yj.board.exception.member.LoginFailException;
import yj.board.jwt.JwtProperties;
import yj.board.service.TokenService;

import javax.servlet.http.Cookie;
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
    public static final String TEST_ID = "loginTest";
    public static final String TEST_PW = "testTest1";
    public static final String TEST_ACCESS_TOKEN = "testat";
    public static final String TEST_REFRESH_TOKEN = "testrt";

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
                .andExpect(header().exists(JwtProperties.HEADER_STRING))
                .andExpect(header().string(JwtProperties.HEADER_STRING, TEST_ACCESS_TOKEN))
                .andExpect(cookie().exists(JwtProperties.REFRESH_HEADER_STRING))
                .andExpect(cookie().value(JwtProperties.REFRESH_HEADER_STRING, TEST_REFRESH_TOKEN));
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
        given(tokenService.reissue(any(TokenDto.class)))
                .willReturn(ReissueTokenDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).memberDto(getMemberDto()).build());
        //when
        ResultActions actions = requestRegisterUrl(object, "/token", "put");

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(JwtProperties.HEADER_STRING))
                .andExpect(header().string(JwtProperties.HEADER_STRING, newAccessToken))
                .andExpect(cookie().exists(JwtProperties.REFRESH_HEADER_STRING))
                .andExpect(cookie().value(JwtProperties.REFRESH_HEADER_STRING, newRefreshToken))
                ;
    }

    private ResultActions requestRegisterUrl(String object, String url, String method) throws Exception {
        ResultActions actions;

        if (method.equals("put")) {
            actions = mockMvc.perform(put(url)
                    .cookie(new Cookie(JwtProperties.REFRESH_HEADER_STRING, TEST_REFRESH_TOKEN)) // 쿠키 추가
                    .content(object)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));
        } else {
            actions = mockMvc.perform(post(url)
                    .content(object)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        return actions;
    }

    private static LoginDto getLoginDto() {
        LoginDto loginDto = new LoginDto(TEST_ID, TEST_PW);
        return loginDto;
    }

    private static TokenDto getTokenDto() {
        TokenDto tokenDto = TokenDto.builder()
                .accessToken(TEST_ACCESS_TOKEN)
                .refreshToken(TEST_REFRESH_TOKEN)
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
