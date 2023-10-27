package yj.board.exception;

public class MemberUpdateException extends RuntimeException{
    public MemberUpdateException() {
        super();
    }
    public MemberUpdateException(String message) { super(message); }
    public MemberUpdateException(String message, Throwable cause) { super(message, cause); }
}
