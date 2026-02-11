package in.sp.main.customeexception;

public class WrongAmountException extends RuntimeException{

    public WrongAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongAmountException(String message) {
        super(message);
    }
}
