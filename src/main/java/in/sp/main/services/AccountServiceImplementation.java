package in.sp.main.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import in.sp.main.customeexception.CustomerNotFoundException;
import in.sp.main.customeexception.InsufficientBalanceException;
import in.sp.main.customeexception.WrongAmountException;
import in.sp.main.model.TransactionDetails;
import in.sp.main.repository.TransactionRepository;
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

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository txtDetailRepo;

	@Autowired
	TransactionDetails transactionDetails;

	@Override
	public boolean validateCustomer(int id) {
		return accountRepository.findById(id).isEmpty();
	}

	// Method for the Create The customer Account
	@Override
	public Account createAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public TransactionDetails paymentDetails(TransactionDetails transactionDetails) {
		return txtDetailRepo.save(transactionDetails);
	}

	// Method to get account based on their id
	@Override
	public Optional<Account> getAccountByID(int id) {
		if (validateCustomer(id))
			throw new CustomerNotFoundException("Requested user is not exist");

		return accountRepository.findById(id);
	}

	// Method for get all accounts details
	@Override
	public List<Account> getAllAccountDetails() {
		if (accountRepository.findAll().isEmpty())
			throw new CustomerNotFoundException("Requested data not found");
		return accountRepository.findAll();
	}

	// Method for deleting the account based on customer id
	@Override
	public void deleteAccountByID(int id) {
		if (validateCustomer(id))
			throw new CustomerNotFoundException("Requested user is not exist");
		accountRepository.deleteById(id);
	}

	// Method for updating the existing user details
	@Override
	public Account updateAccount(int id, Account account) {
		Account userExistData = accountRepository.findById(id).get();
		if (validateCustomer(id))
			throw new CustomerNotFoundException("Requested user is not exist");

		return accountRepository.save(userExistData);

	}

	// Method for withdraw amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> withdrawAmount(int id, BigDecimal withdrawAmount) {

		if(withdrawAmount.compareTo(BigDecimal.ZERO) <= 0){
			throw new WrongAmountException("Please enter the positive amount");
		}

		Optional<Account> accountDetails = accountRepository.findById(id);
		if (!accountDetails.isPresent()) {
			throw new CustomerNotFoundException("Requested user is not exist");
		}
		Account getExistCustomerObj = accountDetails.get();

		BigDecimal currentBalance = getExistCustomerObj.getBalance();

		if (withdrawAmount.compareTo(currentBalance) > 0 ){
			throw new InsufficientBalanceException("Insufficient balance");
		}

		BigDecimal newBalance = currentBalance.subtract(withdrawAmount);
		getExistCustomerObj.setBalance(newBalance);
		accountRepository.save(getExistCustomerObj);

		if (withdrawAmount.compareTo(new BigDecimal(111)) == 0) {
			throw new RuntimeException();
		}

		return ResponseEntity.status(HttpStatus.OK).body("WithdrawAmount : " + withdrawAmount + " \n" + "New Balance : " + newBalance);

	}

	// Method for deposit amount from the existing account
	@Transactional
	@Override
	public ResponseEntity<Object> depositAmount(int id, BigDecimal depositAmount) {
		if (validateCustomer(id))
			throw new CustomerNotFoundException("Account Not Exist");

		if(depositAmount.compareTo(BigDecimal.ZERO) < 0){
			throw new WrongAmountException("Please Enter positive Amount for withdrawal");
		}

		Account getExistUserDetails = accountRepository.findById(id).get();
		BigDecimal availableBalance = getExistUserDetails.getBalance();
		if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new WrongAmountException("Please enter a valid amount");
		}else if (depositAmount.compareTo(new BigDecimal("40000")) > 0) {
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
		return ResponseEntity.ok().body("Deposit Amount : " + depositAmount + ", " + "New Balance : " + newBalance);
	}

	@Override
	public BigDecimal checkBalance(int id) {
		if (validateCustomer(id))
			throw new CustomerNotFoundException("Account Not Exist");
		Account getExistUserDetails = accountRepository.findById(id).get();
		return getExistUserDetails.getBalance();

	}

	@Transactional
	@Override
	public ResponseEntity<Object> transferMoney(int fromId, int toId, BigDecimal amount) {
		String txtDetails = UUID.randomUUID().toString();

		transactionDetails.setPaymentTxtId(txtDetails);
		transactionDetails.setPaymentFromID(fromId);
		transactionDetails.setPaymentToID(toId);
		transactionDetails.setPaymentTransferAmount(amount);
		transactionDetails.setPaymentTxtDate(Instant.now());

		Account from = accountRepository.findById(fromId)
				.orElseThrow(() -> new CustomerNotFoundException("From account not found"));

		Account to = accountRepository.findById(toId)
				.orElseThrow(() -> new CustomerNotFoundException("To account not found"));

		if (from.getBalance().compareTo(amount) < 0) {
			transactionDetails.setPaymentTxtStatus("FAILED");
			transactionDetails.setPaymentStatusReason("Insufficient balance of sender");
			transactionDetails.setPaymentFromName(from.getAccountHolderName());
			transactionDetails.setPaymentToName(to.getAccountHolderName());
			paymentDetails(transactionDetails);
			return ResponseEntity.badRequest().body("Insufficient balance " + fromId );
		}



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

		paymentDetails(transactionDetails);


		return ResponseEntity.ok().body("Transfer Money from id :" + fromId + " To :" + toId + " Successfully");
	}




}
