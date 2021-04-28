package net.sejongtelecom.backendapi.domain.snapshot.mapper;

import net.sejongtelecom.backendapi.domain.snapshot.dto.SnapshotMetaDto;
import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotMetaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Version : 1.0
 * 파일명: SnapshotMetaMapper.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : SnaphostMeta 의 Entity < - > DTO 매핑 클래스
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnapshotMetaMapper {
  SnapshotMetaMapper INSTANCE = Mappers.getMapper(SnapshotMetaMapper.class);

  public SnapshotMetaEntity snapshotMetaDtoToEntity(SnapshotMetaDto snapshotMetaDto);

  public SnapshotMetaDto entityToSnapshot(SnapshotMetaEntity snapshotMetaEntity);
}
