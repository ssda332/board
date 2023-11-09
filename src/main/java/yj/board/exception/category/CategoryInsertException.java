package yj.board.exception.category;

public class CategoryInsertException extends RuntimeException{
    public CategoryInsertException() {
        super();
    }
    public CategoryInsertException(String message, Throwable cause) {
        super(message, cause);
    }
    public CategoryInsertException(String message) {
        super(message);
    }
    public CategoryInsertException(Throwable cause) {
        super(cause);
    }
}
