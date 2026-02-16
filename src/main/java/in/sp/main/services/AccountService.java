package in.sp.main.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import in.sp.main.model.Account;
import in.sp.main.model.TransactionDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public interface AccountService {
	public Account createAccount(Account account);

	public Account getAccountByID(int id);

	public List<Account> getAllAccountDetails();

	public ResponseEntity<Object> deleteAccountByID(int id);

	public Account updateAccount(int id, Account account);

	public ResponseEntity<Object> withdrawAmount(int id, BigDecimal WithdrawAmount);

	public ResponseEntity<Object> depositAmount(int id, BigDecimal depositAmount);

	public BigDecimal checkBalance(int id);
	
	public Account validateCustomer(int id);

	public ResponseEntity<Object> transferMoney(int fromId, int toId, BigDecimal amount);

	public TransactionDetails paymentDetails(TransactionDetails transactionDetails);

	public ResponseEntity<Object> deleteCustomerById(int id);
}
