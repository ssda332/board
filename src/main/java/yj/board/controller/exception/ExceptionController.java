package yj.board.controller.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yj.board.exception.member.CAccessDeniedException;
import yj.board.exception.member.CAuthenticationEntryPointException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entryPoint")
    public ResponseEntity entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping("/accessDenied")
    public ResponseEntity accessDeniedException() {
        throw new CAccessDeniedException();
    }
}