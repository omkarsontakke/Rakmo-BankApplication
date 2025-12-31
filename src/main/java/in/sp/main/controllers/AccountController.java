package in.sp.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.customeresponse.ResponseHandler;
import in.sp.main.model.Account;
import in.sp.main.services.AccountService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping
	public Account createCustomerAccount(@RequestBody Account account) {
		return accountService.createAccunt(account);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getAccountDetailsByID(@PathVariable int id) {
		return ResponseHandler.responseBuilder("Requested Data are given here",
				HttpStatus.OK, accountService.getAccountByID(id));
		
	}
	 
	@GetMapping("/getAllAccountDetails")
	public ResponseEntity<Object> getAllAccountDetails() {
		return ResponseHandler.responseBuilder("All Accounts Details are give here",
				HttpStatus.OK, accountService.getAllAccountDetails());
	}
	
	@DeleteMapping("/{id}")
	public String deleteAccountById(@PathVariable int id) {
		accountService.deleteAccountByID(id);
		return "Account Deleted Succesffuly";
	}
	
	//Updating full object and save it 
	@PutMapping("/{id}")
	public Account updateAccount(@PathVariable int id, @RequestBody Account account) {
		return accountService.updateAccount(id, account);
	}
	
	@PutMapping("/withdraw/{id}/{amount}")
	public String withdraAmount(@PathVariable int id, @PathVariable double amount) {
		return accountService.withdrawAmount(id, amount);
	}
	
	@PutMapping("/deposite/{id}/{amount}")
	public ResponseEntity<Object> depositeAmount(@PathVariable int id, @PathVariable double amount) {
		return ResponseHandler.responseBuilder("Cash Deposited Successfully",
				HttpStatus.OK, accountService.depositeAmount(id, amount));
		
	}
	
}
