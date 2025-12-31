package in.sp.main.customeexception;

public class GlobalCustomeExceptions extends RuntimeException{

	public GlobalCustomeExceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public GlobalCustomeExceptions(String message) {
		super(message);
	}
	
}
