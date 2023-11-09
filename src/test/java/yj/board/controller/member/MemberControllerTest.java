package yj.board.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import yj.board.domain.member.dto.MemberDto;
import yj.board.exception.member.DuplicateMemberException;
import yj.board.service.MemberService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccess() throws Exception {
        //given
        MemberDto memberDto = getMemberDto();
//        String object = objectMapper.writeValueAsString(memberDto);
        String object = "{\"loginId\":\"conTest1\",\"password\":\"conTest1\",\"nickname\":\"conTest1\",\"authorityDtoSet\":[{\"authorityName\":\"ROLE_USER\"}]}";
        given(memberService.signup(any(MemberDto.class))).willReturn(memberDto);

        ResultActions actions = requestRegisterUrl(object, "/members");

        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("loginId", "conTest1").exists())
                .andExpect(jsonPath("nickname", "conTest1").exists())
                .andExpect(jsonPath("authorityDtoSet[0].authorityName").value("ROLE_USER"));
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 입력")
    public void signUpFail_wrongInput() throws Exception {
        MemberDto memberDto = getMemberDto();
//        String object = objectMapper.writeValueAsString(memberDto);
        String object = "{\"loginId\":\"\",\"password\":\"\",\"nickname\":\"conTest1\",\"authorityDtoSet\":[{\"authorityName\":\"ROLE_USER\"}]}";
        given(memberService.signup(any(MemberDto.class))).willReturn(memberDto);

        ResultActions actions = requestRegisterUrl(object, "/members");

        //then
        String errorCode = "$.[?(@.code == '%s')]";
        String errorStatus = "$.[?(@.status == '%s')]";

        actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(errorCode, "CM_001").exists())
                .andExpect(jsonPath(errorStatus, 400).exists());

    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자")
    public void signUpFail_duplicateMember() throws Exception {
        MemberDto memberDto = getMemberDto();
//        String object = objectMapper.writeValueAsString(memberDto);
        String object = "{\"loginId\":\"conTest1\",\"password\":\"conTest1\",\"nickname\":\"conTest1\",\"authorityDtoSet\":[{\"authorityName\":\"ROLE_USER\"}]}";
        given(memberService.signup(any(MemberDto.class))).willThrow(new DuplicateMemberException());

        ResultActions actions = requestRegisterUrl(object, "/members");

        //then
        String errorCode = "$.[?(@.code == '%s')]";
        String errorStatus = "$.[?(@.status == '%s')]";

        actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(errorCode, "CM_002").exists())
                .andExpect(jsonPath(errorStatus, 409).exists());

    }

    @Test
    @DisplayName("회원 정보 변경 - 닉네임 변경 성공")
    public void updateMember_success() throws Exception {
        MemberDto memberDto = getMemberDto();

    }

    private ResultActions requestRegisterUrl(String object, String url) throws Exception {
        ResultActions actions = mockMvc.perform(post(url)
                .content(object)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions = mockMvc.perform(post(url)
                .content(object)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        return actions;
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
