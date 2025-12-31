package in.sp.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.customeexception.GlobalCustomeExceptions;
import in.sp.main.model.Account;
import in.sp.main.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImplementation implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public boolean validateCustomer(int id) {
		return accountRepository.findById(id).isEmpty();
	}

	// Method for the Create The customer Account
	@Override
	public Account createAccunt(Account account) {
		return accountRepository.save(account);
	}

	// Method to get account based on their id
	@Override
	public Optional<Account> getAccountByID(int id) {
		if (validateCustomer(id))
			throw new GlobalCustomeExceptions("Requested User Is Not Exist");

		return accountRepository.findById(id);
	}

	// Method for get all accounts details
	@Override
	public List<Account> getAllAccountDetails() {
		if (accountRepository.findAll().isEmpty())
			throw new GlobalCustomeExceptions("Data Not Found");
		return accountRepository.findAll();
	}

	// Method for deleting the account based on customer id
	@Override
	public void deleteAccountByID(int id) {
		if (validateCustomer(id))
			throw new GlobalCustomeExceptions("User Not Exist");
		accountRepository.deleteById(id);
	}

	// Method for updating the existing user details
	@Override
	public Account updateAccount(int id, Account account) {
		Account userExistData = accountRepository.findById(id).get();
		if (validateCustomer(id))
			throw new GlobalCustomeExceptions("Requested User Is Not Exist");

		return accountRepository.save(userExistData);

	}

	// Method for withdraw amount from the existing account
	@Transactional
	@Override
	public String withdrawAmount(int id, double withdraAmount) {
		
		Account getExistCustomerObj = accountRepository.findById(id).get();

		if (validateCustomer(id))
			throw new GlobalCustomeExceptions("Account Not Exist");

		double currentBalance = getExistCustomerObj.getBalance();

		if ((withdraAmount > currentBalance) || (currentBalance == 0) ) {
			return "Insuffcient Balance";
		}else if(withdraAmount == 0) {
			return "Please Enter valid Amount";
		}
		double newBalance = currentBalance - withdraAmount;
		getExistCustomerObj.setBalance(Math.round(newBalance));
		accountRepository.save(getExistCustomerObj);
		return "WithdraAmount : " + withdraAmount + " \n" + "New Balance : " + newBalance;
	}

	// Method for deposite amount from the existing account
	@Transactional
	@Override
	public String depositeAmount(int id, double depositeAmount) {
		if (validateCustomer(id))
			throw new GlobalCustomeExceptions("Account Not Exist");
		Account getExistUserDetails = accountRepository.findById(id).get();
		double availableBalance = getExistUserDetails.getBalance();
		if (depositeAmount > 40000) {
			throw new GlobalCustomeExceptions("You can only deposit 40000 in a Day");
		} else if (depositeAmount <= 0) {
			throw new GlobalCustomeExceptions("Please Enter Valid Amount");
		}
		double newBalance = depositeAmount + availableBalance;
		getExistUserDetails.setBalance(Math.round(newBalance));
		accountRepository.save(getExistUserDetails);

		if (depositeAmount == 111) {
			throw new RuntimeException();
		}

		return "Deposit Amount : " + depositeAmount + ", " + "New Balance : " + newBalance;
	}
}
