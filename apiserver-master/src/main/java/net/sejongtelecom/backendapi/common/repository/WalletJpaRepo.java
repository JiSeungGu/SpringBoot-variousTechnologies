package net.sejongtelecom.backendapi.common.repository;

import java.util.Optional;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepo extends JpaRepository<Wallet, Long> {

  Optional<Wallet> findByUserid(String userid);

  long countByUserid(String userid);

  Optional<Wallet> findByWaddress(String waddress);
}
