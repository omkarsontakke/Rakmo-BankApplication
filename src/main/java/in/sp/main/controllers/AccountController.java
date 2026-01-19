package in.sp.main.controllers;

import in.sp.main.Util.TransferRequest;
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

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;

	// Endpoint for creating the new customer in the database
	@PostMapping
	public Account createCustomerAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}

	// Endpoint for getting customer by id
	@GetMapping("/{id}")
	public ResponseEntity<Object> getAccountDetailsByID(@PathVariable int id) {
		return ResponseHandler.responseBuilder("Requested Data are given here",
				HttpStatus.OK, accountService.getAccountByID(id));
		
	}

	// Endpoint for getting all customers
	@GetMapping("/getAllAccountDetails")
	public ResponseEntity<Object> getAllAccountDetails() {
		return ResponseHandler.responseBuilder("All Accounts Details are give here",
				HttpStatus.OK, accountService.getAllAccountDetails());
	}

	// Endpoint for Delete existing customer
	@DeleteMapping("/{id}")
	public String deleteAccountById(@PathVariable int id) {
		accountService.deleteAccountByID(id);
		return "Account Deleted Successfully";
	}
	
	//Updating full object and save it 
	@PutMapping("/{id}")
	public Account updateAccount(@PathVariable int id, @RequestBody Account account) {
		return accountService.updateAccount(id, account);
	}

	// Endpoint for Withdraw the amount from the existing customer
	@PutMapping("/withdraw/{id}/{amount}")
	public ResponseEntity<Object> withdrawAmount(@PathVariable int id, @PathVariable BigDecimal amount) {
//		return ResponseHandler.responseBuilder("Cash Withdraw Successfully",
//				HttpStatus.OK, accountService.withdrawAmount(id, amount));

		return accountService.withdrawAmount(id, amount);
	}

	// Endpoint for Deposit the amount in the existing customer
	@PutMapping("/deposit/{id}/{amount}")
	public ResponseEntity<Object> depositAmount(@PathVariable int id, @PathVariable BigDecimal amount) {
		return accountService.depositAmount(id, amount.setScale(2, RoundingMode.UP));
		
	}

	// Endpoint for the Checking balance of the existing customer
	@GetMapping("/checkBalance/{id}")
	public ResponseEntity<Object> checkBalance(@PathVariable int id){
		return ResponseHandler.responseBuilder("Balance Fetched Successfully",
				HttpStatus.OK, accountService.checkBalance(id));

//		return ResponseEntity.ok("");
	}


	// Endpoint for transferring the money from one account to another account

	@PostMapping("/transfer")
	public ResponseEntity<Object> transferMoney(@RequestBody TransferRequest transferRequest){

		return ResponseHandler.responseBuilder("Balance Fetched Successfully",
				HttpStatus.OK, accountService.transferMoney(
						transferRequest.getFromAccountId(),
						transferRequest.getToAccountId(),
						transferRequest.getAmount()));

	}
}
