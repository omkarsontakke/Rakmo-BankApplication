package in.sp.main.customeresponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	public static ResponseEntity<Object> responseBuilder(
			String message, HttpStatus httpStatus, Object responseObj
	)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("HttpStatus", httpStatus);
		response.put("message", message);
		response.put("data", responseObj);
		
		return new ResponseEntity<>(response, httpStatus);
	}

	public static ResponseEntity<Object> basicResponseBuilder(
			String message, HttpStatus httpStatus
	)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("HttpStatus", httpStatus);
		response.put("message", message);

		return new ResponseEntity<>(response, httpStatus);
	}
}