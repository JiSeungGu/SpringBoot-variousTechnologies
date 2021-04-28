package net.sejongtelecom.backendapi.common.repository;

import java.util.Optional;
import net.sejongtelecom.backendapi.common.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeJpaRepo extends JpaRepository<CommonCode, Long> {

  Optional<CommonCode> findByCodeid(String codeid);

}
