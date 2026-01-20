package in.sp.main.repository;

import in.sp.main.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionDetails, String> {
}
