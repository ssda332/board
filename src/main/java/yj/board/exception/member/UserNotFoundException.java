package yj.board.exception.member;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super();
    }
    public UserNotFoundException(String message) { super(message); }
    public UserNotFoundException(String message, Throwable cause) { super(message, cause); }
}
