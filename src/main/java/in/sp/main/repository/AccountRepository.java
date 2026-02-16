package in.sp.main.repository;

import in.sp.main.model.TransactionDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import in.sp.main.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.id = :id")
	int deleteAccountById(int id);
}


