package in.sp.main.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import in.sp.main.model.Account;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public interface AccountService {
	public Account createAccount(Account account);

	public Optional<Account> getAccountByID(int id);

	public List<Account> getAllAccountDetails();

	public void deleteAccountByID(int id);

	public Account updateAccount(int id, Account account);

	public String withdrawAmount(int id, BigDecimal WithdrawAmount);


	public String depositAmount(int id, BigDecimal depositAmount);
	
	public boolean validateCustomer(int id);


}
