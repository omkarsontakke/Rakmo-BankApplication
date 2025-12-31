package in.sp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.sp.main.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
}
