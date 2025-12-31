package in.sp.main.customeexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = {GlobalCustomeExceptions.class})
	public ResponseEntity<Object> handleGlobalException
	(GlobalCustomeExceptions globalCustomeExceptions){
		GlobalException globalException = new GlobalException
				(globalCustomeExceptions.getMessage(),
					globalCustomeExceptions.getCause(), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(globalException, HttpStatus.NOT_FOUND);
		
	}
	
}
