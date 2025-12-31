package in.sp.main.customeexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = {GlobalCustomExceptions.class})
	public ResponseEntity<Object> handleGlobalException
	(GlobalCustomExceptions globalCustomExceptions){
		GlobalException globalException = new GlobalException
				(globalCustomExceptions.getMessage(),
						globalCustomExceptions.getCause(), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(globalException, HttpStatus.NOT_FOUND);
		
	}
	
}
