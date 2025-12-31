package in.sp.main.customeexception;

public class GlobalCustomExceptions extends RuntimeException{

	public GlobalCustomExceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public GlobalCustomExceptions(String message) {
		super(message);
	}
	
}
