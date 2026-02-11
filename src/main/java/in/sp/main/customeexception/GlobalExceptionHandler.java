package in.sp.main.customeexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {CustomerNotFoundException.class})
	public ResponseEntity<Object> customerNotFoundException
	(CustomerNotFoundException customerNotFoundException){
		GlobalException customerException = new GlobalException
				(	customerNotFoundException.getMessage(),
					customerNotFoundException.getCause(),
					HttpStatus.NOT_FOUND
				);

		return new ResponseEntity<>(customerException, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(value = {WrongAmountException.class})
	public ResponseEntity<Object> customerUnproccessableEntity
			(WrongAmountException wrongAmountException){
		GlobalException customerException = new GlobalException
				(	wrongAmountException.getMessage(),
						wrongAmountException.getCause(),
						HttpStatus.UNPROCESSABLE_ENTITY
				);

		return new ResponseEntity<>(customerException, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(value = {InsufficientBalanceException.class})
	public ResponseEntity<Object> balanceInsufficientException
			(InsufficientBalanceException insufficientBalanceException){
		GlobalException customerException = new GlobalException
				(	insufficientBalanceException.getMessage(),
					insufficientBalanceException.getCause(),
					HttpStatus.UNPROCESSABLE_ENTITY
				);

		return new ResponseEntity<>(customerException, HttpStatus.UNPROCESSABLE_ENTITY);

	}

}
