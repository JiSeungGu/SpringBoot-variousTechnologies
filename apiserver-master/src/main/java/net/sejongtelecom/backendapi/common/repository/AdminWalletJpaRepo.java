package net.sejongtelecom.backendapi.common.repository;

import java.util.Optional;
import net.sejongtelecom.backendapi.common.entity.AdminWallet;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminWalletJpaRepo extends JpaRepository<AdminWallet, Long> {

  Optional<AdminWallet> findByFundid(String fundid);

  long countByFundid(String fundid);

  Optional<AdminWallet> findByWaddress(String waddress);
}
