package net.sejongtelecom.backendapi.domain.snapshot.repository;

import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Version : 1.0
 * 파일명: SnapshotJpaRepo.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : Snaphost JPA 레포지토리
 */
public interface SnapshotJpaRepo extends JpaRepository<SnapshotEntity, Long> {
  SnapshotEntity findByFundidAndSnapshotidAndAddress(String fundid, String snapshotid, String address);

    SnapshotEntity findByStnameAndCodeAndAddress(String dtoken, String snapshotcode, String waddress);
}