package net.sejongtelecom.backendapi.domain.snapshot.repository;

import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Version : 1.0
 * 파일명: SnapshotMetaJpaRepo.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : SnaphostMeta JPA 레포지토리
 */
public interface SnapshotMetaJpaRepo extends JpaRepository<SnapshotMetaEntity, Long> {

  long countByFundidAndSnapshotid(String fundid, String snapshotid);

  SnapshotMetaEntity findByFundidAndSnapshotid(String fundid, String snapshotid);

}