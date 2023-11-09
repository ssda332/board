package yj.board.exception.category;

public class CategoryDeleteException extends RuntimeException{
    public CategoryDeleteException() {
        super();
    }
    public CategoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
    public CategoryDeleteException(String message) {
        super(message);
    }
    public CategoryDeleteException(Throwable cause) {
        super(cause);
    }
}
