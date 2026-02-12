package in.sp.main.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import in.sp.main.controllers.AccountController;
import in.sp.main.customeexception.CustomerNotFoundException;
import in.sp.main.customeexception.InsufficientBalanceException;
import in.sp.main.customeexception.WrongAmountException;
import in.sp.main.model.TransactionDetails;
import in.sp.main.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import in.sp.main.customeexception.GlobalCustomExceptions;
import in.sp.main.model.Account;
import in.sp.main.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImplementation implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImplementation.class);


	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository txtDetailRepo;

	@Autowired
	TransactionDetails transactionDetails;

	// This Method will check that user is exist in the database or not.
	@Override
	public Account validateCustomer(int id) {
        logger.info("Validating customer for id : {}", id);

		Account accountDetails = accountRepository.findById(id)
				.orElseThrow( () -> {
					logger.error("Customer not exist for the id: {}", id);
					return new CustomerNotFoundException("Customer not exist for id: " + id);
				});

		logger.info("Customer id: {} validate successfully.", id);

		return accountDetails;
	}

	// Method for the Create The customer Account
	@Override
	public Account createAccount(Account account) {
		logger.info("Inside Create Account Method");
		Account savedAccount = accountRepository.save(account);
		logger.info("Account created successfully for the customer: {}", savedAccount.getAccountHolderName());
		return savedAccount;
	}

	// This method will save the transaction details for sending money in database.
	@Override
	public TransactionDetails paymentDetails(TransactionDetails transactionDetails) {
		logger.info("Saving txt details in the database");
		return txtDetailRepo.save(transactionDetails);
	}

	// Method to get account based on their id
	@Override
	public Account getAccountByID(int id) {
		logger.info("Inside account by id method");
		return validateCustomer(id);
	}

	// Method for get all accounts details
	@Override
	public List<Account> getAllAccountDetails() {
		if (accountRepository.findAll().isEmpty()) {
			logger.error("Customers not present in the database");
			throw new CustomerNotFoundException("Requested data not found");
		}
		logger.info("Fetch the all accounts details");
		return accountRepository.findAll();
	}

	// Method for deleting the account based on customer id
	@Override
	public ResponseEntity<Object> deleteAccountByID(int id) {
		validateCustomer(id);

		accountRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully delete customer account with id : "+id);
	}

	// Method for updating the existing user details
	@Override
	public Account updateAccount(int id, Account account) {
		Account userExistData = accountRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Requested user is not exist"));
		logger.info("Account update successfully for the id : {}",id);
		return accountRepository.save(userExistData);

	}

	// Method for withdraw amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> withdrawAmount(int id, BigDecimal withdrawAmount) {

		logger.info("Inside Withdraw method for id : {}",id);

		Account getExistCustomerObj = accountRepository.findById(id)
				.orElseThrow( () -> new CustomerNotFoundException("Account not found with id: " + id));

		if(withdrawAmount.compareTo(BigDecimal.ZERO) <= 0){
			logger.info("Amount can not negative or zero");
			throw new WrongAmountException("Please enter the positive amount");
		}


		BigDecimal currentBalance = getExistCustomerObj.getBalance();

		if (withdrawAmount.compareTo(currentBalance) > 0 ){
			logger.info("Customer with id : {} have less balance for withdrawal", id);
			throw new InsufficientBalanceException("Insufficient balance");
		}

		BigDecimal newBalance = currentBalance.subtract(withdrawAmount);
		getExistCustomerObj.setBalance(newBalance);
		accountRepository.save(getExistCustomerObj);

		if (withdrawAmount.compareTo(new BigDecimal(111)) == 0) {
			throw new RuntimeException();
		}

		logger.info("Withdraw money successfully for the customer id : {}",id);
		return ResponseEntity.status(HttpStatus.OK).body("WithdrawAmount : " + withdrawAmount + " \n" + "New Balance : " + newBalance);

	}

	// Method for deposit amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> depositAmount(int id, BigDecimal depositAmount) {

		logger.info("Deposit the amount for account id : {} ",id);

		if(depositAmount.compareTo(BigDecimal.ZERO) <= 0){
			logger.info("Amount can not negative or zero");
			throw new WrongAmountException("Please Enter positive Amount for Deposit");
		}

		Account getExistUserDetails = accountRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Account Not Exist for id :"+id));
		BigDecimal availableBalance = getExistUserDetails.getBalance();

		if (depositAmount.compareTo(new BigDecimal("40000")) > 0) {
			logger.info("Customer with id: {} can only deposit 40000 per day",id);
			throw new WrongAmountException("You can only deposit 40,000 in a day");
		}

//		BigDecimal newBalance = depositAmount + availableBalance;
		BigDecimal newBalance = availableBalance.add(depositAmount);
		getExistUserDetails.setBalance(newBalance);
		accountRepository.save(getExistUserDetails);

//		if (depositAmount == 111) {
//			throw new RuntimeException();
//		}

		if (depositAmount.compareTo(new BigDecimal("111")) == 0) {
			throw new RuntimeException();
		}

		logger.info("Deposit money successfully for the customer id : {}",id);
		return ResponseEntity.ok().body("Deposit Amount : " + depositAmount + ", " + "New Balance : " + newBalance);
	}

	@Override
	public BigDecimal checkBalance(int id) {
		logger.info("Fetching the balance for the id : {}",id);
		Account getExistUserDetails = accountRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Account Not Exist for id :"+id));;
		logger.info("Balance fetech successfully for the customer id : {}", id);
		return getExistUserDetails.getBalance();

	}

	@Transactional
	@Override
	public ResponseEntity<Object> transferMoney(int fromId, int toId, BigDecimal amount) {
		logger.info("Generating transaction unique id");
		String txtDetails = UUID.randomUUID().toString();

		transactionDetails.setPaymentTxtId(txtDetails);
		transactionDetails.setPaymentFromID(fromId);
		transactionDetails.setPaymentToID(toId);
		transactionDetails.setPaymentTransferAmount(amount);
		transactionDetails.setPaymentTxtDate(Instant.now());

		logger.info("Fetching FROM account customer details");
		Account from = accountRepository.findById(fromId)
				.orElseThrow(() -> new CustomerNotFoundException("From account not found"));

		logger.info("Fetching TO account customer details");
		Account to = accountRepository.findById(toId)
				.orElseThrow(() -> new CustomerNotFoundException("To account not found"));

		logger.info("Checking balance for the FROM customer");
		if (from.getBalance().compareTo(amount) < 0) {
			transactionDetails.setPaymentTxtStatus("FAILED");
			transactionDetails.setPaymentStatusReason("Insufficient balance of sender");
			transactionDetails.setPaymentFromName(from.getAccountHolderName());
			transactionDetails.setPaymentToName(to.getAccountHolderName());
			paymentDetails(transactionDetails);
			return ResponseEntity.badRequest().body("Insufficient balance " + fromId );
		}


		logger.info("Subtracting the balance of FROM customer");
		from.setBalance(from.getBalance().subtract(amount));
		to.setBalance(to.getBalance().add(amount));
		accountRepository.save(from);

		if (amount.compareTo(new BigDecimal("111")) == 0) {
			transactionDetails.setPaymentTxtStatus("FAILED");
			transactionDetails.setPaymentStatusReason("Payment Failed Due to amount");
			transactionDetails.setPaymentFromName(from.getAccountHolderName());
			transactionDetails.setPaymentToName(to.getAccountHolderName());
			paymentDetails(transactionDetails);
			throw new RuntimeException("Transaction Management Test");
//			return ResponseEntity.badRequest().body("Transaction Management Test");
		}
		accountRepository.save(to);

		transactionDetails.setPaymentTxtStatus("SUCCESS");
		transactionDetails.setPaymentStatusReason("Transfer Money from id " + fromId + " To" + toId + " Successfully");
		transactionDetails.setPaymentFromName(from.getAccountHolderName());
		transactionDetails.setPaymentToName(to.getAccountHolderName());

		logger.info("Saving transaction details for the request");
		paymentDetails(transactionDetails);
		logger.info("Transaction details save for request");

		logger.info("Transfer money request success");
		return ResponseEntity.ok().body("Transfer Money from id :" + fromId + " To :" + toId + " Successfully");
	}




}
