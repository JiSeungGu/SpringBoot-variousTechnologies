package net.sejongtelecom.backendapi.common.repository;

import java.util.Optional;
import net.sejongtelecom.backendapi.common.entity.Cdmaster;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CdmasterJpaRepo extends JpaRepository<Cdmaster, Long> {

  Optional<Cdmaster> findByFundid(String fundid);

  long countByFundid(String fundid);
}
