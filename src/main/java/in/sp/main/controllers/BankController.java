package in.sp.main.controllers;

import in.sp.main.customeresponse.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank/employee")
public class BankController {

    @GetMapping()
    public String greeting() {
        return "Welcome to the Bank RESTful Web Service";
    }

}
