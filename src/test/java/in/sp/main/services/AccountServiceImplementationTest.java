package in.sp.main.services;

import in.sp.main.model.Account;
import in.sp.main.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplementationTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImplementation accountServiceImplementation;

    @Test
    void createAccountShouldAddNewAccountSuccessfully() {
        System.out.println("Welcome to the first unit test");
        Account account = new Account();

        account.setId(7);
        account.setBalance(100);
        account.setAccountStatus("active");
        account.setAccountType("bank");
        account.setAccountHolderName("John Doe");
        account.setIfsc_code("1234");
        account.setBranchName("Branch Name");
        account.setCustomer_id(7);

        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Account addAccount =  accountServiceImplementation.createAccount(account);

        Assertions.assertEquals(7, addAccount.getId());

        Mockito.verify(accountRepository, Mockito.times(1)).save(account);

    }
}