package in.sp.main.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import in.sp.main.controllers.AccountController;
import in.sp.main.customeexception.*;
import in.sp.main.customeresponse.ResponseHandler;
import in.sp.main.model.TransactionDetails;
import in.sp.main.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import in.sp.main.model.Account;
import in.sp.main.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImplementation implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImplementation.class);
	private static final BigDecimal DAILY_DEPOSIT_LIMIT = BigDecimal.valueOf(40_000);



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
					logger.info("Customer not exist for the id: {}", id);
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

	@Override
	public ResponseEntity<Object> deleteCustomerById(int id) {



		return null;
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

		logger.info("Initiating the delete request for id : {}" ,id);

		int deleteAccount =  accountRepository.deleteAccountById(id);

		if(deleteAccount == 0){
			logger.info("Customer not exist for id : "+id);
			throw new CustomerNotFoundException("Customer account not found with id: " + id);
		}

		logger.info("Account delete successfully for the id : "+id);
		return ResponseEntity.noContent().build();

	}

	// Method for updating the existing user details
	@Override
	public Account updateAccount(int id, Account account) {
		validateCustomer(id);
		logger.info("Account update successfully for the id : {}",id);
		return accountRepository.save(account);

	}

	// Method for withdraw amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> withdrawAmount(int id, BigDecimal withdrawAmount) {

		logger.info("Inside Withdraw method for id : {}",id);

		if(withdrawAmount.signum() <= 0){
			logger.info("Amount can not negative or zero");
			throw new WrongAmountException("Please enter the positive amount");
		}

		Account getExistCustomerObj = validateCustomer(id);

		BigDecimal currentBalance = getExistCustomerObj.getBalance();

		if (withdrawAmount.compareTo(currentBalance) > 0 ){
			logger.info("Customer with id : {} have less balance for withdrawal", id);
			throw new InsufficientBalanceException("Insufficient balance");
		}

		BigDecimal newBalance = currentBalance.subtract(withdrawAmount);
		getExistCustomerObj.setBalance(newBalance);
		accountRepository.save(getExistCustomerObj);

		logger.info("Withdraw money successfully for the customer id : {}",id);
		return ResponseEntity.status(HttpStatus.OK).body("WithdrawAmount : " + withdrawAmount + " \n" + "New Balance : " + newBalance);

	}

	// Method for deposit amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> depositAmount(int id, BigDecimal depositAmount) {

		logger.info("Deposit the amount for account id : {} ",id);


		if(depositAmount == null ||  depositAmount.signum() <= 0){
			logger.info("Amount can't negative or zero");
			throw new WrongAmountException("Please Enter positive Amount for Deposit");
		}
		if (depositAmount.compareTo(DAILY_DEPOSIT_LIMIT) > 0) {
			logger.info("Customer with id: {} can only deposit 40000 per day",id);
			throw new WrongAmountException("You can only deposit 40,000 in a day");
		}

		Account getExistUserDetails = validateCustomer(id);

		BigDecimal availableBalance = getExistUserDetails.getBalance();

		BigDecimal newBalance = availableBalance.add(depositAmount);
		getExistUserDetails.setBalance(newBalance);
		accountRepository.save(getExistUserDetails);


		logger.info("Deposit money successfully for the customer id : {}",id);
		return ResponseEntity.ok().body("Deposit Amount : " + depositAmount + ", " + "New Balance : " + newBalance);
	}

	@Override
	public BigDecimal checkBalance(int id) {
		logger.info("Fetching the balance for the id : {}",id);
		Account getExistUserDetails = validateCustomer(id);
		logger.info("Balance fetch successfully for the customer id : {}", id);
		return getExistUserDetails.getBalance();

	}

	private TransactionDetails createTransactionRecord(String txnId, Account from, Account to,
													   BigDecimal amount, String status, String reason) {
		TransactionDetails txn = new TransactionDetails();
		txn.setPaymentTxtId(txnId);
		txn.setPaymentFromID(from.getId());
		txn.setPaymentToID(to.getId());
		txn.setPaymentTransferAmount(amount);
		txn.setPaymentTxtDate(Instant.now());
		txn.setPaymentTxtStatus(status);
		txn.setPaymentStatusReason(reason);
		txn.setPaymentFromName(from.getAccountHolderName());
		txn.setPaymentToName(to.getAccountHolderName());
		return txn;
	}

	@Transactional
	@Override
	public ResponseEntity<Object> transferMoney(int fromId, int toId, BigDecimal amount) {
		logger.info("Generating transaction unique id");
		String txtDetailsID = UUID.randomUUID().toString();
		Instant instantTime = Instant.now();

		logger.info("Fetching FROM account customer details");
		Account from = accountRepository.findById(fromId)
				.orElseThrow(() -> new CustomerNotFoundException("From account not found"));

		logger.info("Fetching TO account customer details");
		Account to = accountRepository.findById(toId)
				.orElseThrow(() -> new CustomerNotFoundException("To account not found"));


		logger.info("Checking balance for the FROM customer");
		if (from.getBalance().compareTo(amount) < 0) {
			TransactionDetails failedTxn = createTransactionRecord(txtDetailsID, from, to, amount, "FAILED",
					"Insufficient balance of sender");
			paymentDetails(failedTxn);
			return ResponseEntity.badRequest().body("Insufficient balance " + fromId );
		}


		// Update balance.
		logger.info("Subtracting the balance of FROM customer");
		from.setBalance(from.getBalance().subtract(amount));
		to.setBalance(to.getBalance().add(amount));

//		accountRepository.saveAll(List.of(to,from));

		TransactionDetails successTxn = createTransactionRecord(txtDetailsID, from, to, amount, "SUCCESS",
				"Transfer successful");

		logger.info("Saving transaction details for the request");
		paymentDetails(successTxn);
		logger.info("Transaction details save for request");

		logger.info("Transfer money request success");
		return ResponseEntity.ok().body("Transfer Money from id :" + fromId + " To :" + toId + " Successfully");
	}




}
