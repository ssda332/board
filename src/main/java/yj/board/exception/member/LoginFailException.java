package yj.board.exception.member;

public class LoginFailException extends RuntimeException{
    public LoginFailException() {
        super();
    }
    public LoginFailException(String message) { super(message); }
    public LoginFailException(String message, Throwable cause) { super(message, cause); }
}
