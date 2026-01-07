package in.sp.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.customeexception.GlobalCustomExceptions;
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
	public Account createAccount(Account account) {
		return accountRepository.save(account);
	}

	// Method to get account based on their id
	@Override
	public Optional<Account> getAccountByID(int id) {
		if (validateCustomer(id))
			throw new GlobalCustomExceptions("Requested User Is Not Exist");

		return accountRepository.findById(id);
	}

	// Method for get all accounts details
	@Override
	public List<Account> getAllAccountDetails() {
		if (accountRepository.findAll().isEmpty())
			throw new GlobalCustomExceptions("Data Not Found");
		return accountRepository.findAll();
	}

	// Method for deleting the account based on customer id
	@Override
	public void deleteAccountByID(int id) {
		if (validateCustomer(id))
			throw new GlobalCustomExceptions("User Not Exist");
		accountRepository.deleteById(id);
	}

	// Method for updating the existing user details
	@Override
	public Account updateAccount(int id, Account account) {
		Account userExistData = accountRepository.findById(id).get();
		if (validateCustomer(id))
			throw new GlobalCustomExceptions("Requested User Is Not Exist");

		return accountRepository.save(userExistData);

	}

	// Method for withdraw amount from the existing account
	@Transactional
	@Override
	public String withdrawAmount(int id, double withdrawAmount) {

		if(withdrawAmount < 0){
			return "Please Enter positive Amount for withdrawal";
		}
		
		Account getExistCustomerObj = accountRepository.findById(id).get();

		if (validateCustomer(id))
			throw new GlobalCustomExceptions("Account Not Exist");

		double currentBalance = getExistCustomerObj.getBalance();

		if ((withdrawAmount > currentBalance) || (currentBalance == 0) ) {
			return "Insufficient Balance";
		}else if(withdrawAmount == 0) {
			return "Please Enter valid Amount";
		}
		double newBalance = currentBalance - withdrawAmount;
		getExistCustomerObj.setBalance(Math.round(newBalance));
		accountRepository.save(getExistCustomerObj);
		return "WithdrawAmount : " + withdrawAmount + " \n" + "New Balance : " + newBalance;
	}

	// Method for deposit amount from the existing account
	@Transactional
	@Override
	public String depositAmount(int id, double depositAmount) {
		if (validateCustomer(id))
			throw new GlobalCustomExceptions("Account Not Exist");
		Account getExistUserDetails = accountRepository.findById(id).get();
		double availableBalance = getExistUserDetails.getBalance();
		if (depositAmount > 40000) {
			throw new GlobalCustomExceptions("You can only deposit 40000 in a Day");
		} else if (depositAmount <= 0) {
			throw new GlobalCustomExceptions("Please Enter Valid Amount");
		}
		double newBalance = depositAmount + availableBalance;
		getExistUserDetails.setBalance(Math.round(newBalance));
		accountRepository.save(getExistUserDetails);

		if (depositAmount == 111) {
			throw new RuntimeException();
		}

		return "Deposit Amount : " + depositAmount + ", " + "New Balance : " + newBalance;
	}
}
