package net.sejongtelecom.backendapi.domain.snapshot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

/**
 * Version : 1.0
 * 파일명: SnapshotMetaDto.java
 * 작성일자 : 2021-3-16
 * 작성자 : 한현수
 * 설명 : 스냅샷 생성을 위한 메타 정보 DTO
 */
@Getter
@Builder
public class SnapshotMetaDto {

  @JsonProperty(value = "fundid")
  @NotEmpty
  @Size(min=2, max=20)
  private String fundid;

  @JsonProperty(value = "snapshotid")
  @NotEmpty
  @Size(min=2, max=50)
  private String snapshotid;

  @JsonProperty(value = "status")
  private String status;

  @JsonProperty(value = "totalcnt")
  private int totalcnt;

  @JsonProperty(value = "create_dt")
  private LocalDateTime create_dt;

  @JsonProperty(value = "updated_dt")
  private LocalDateTime updated_dt;

  @Builder
  public SnapshotMetaDto (String fundid, String snapshotid, String status, int totalcnt, LocalDateTime create_dt, LocalDateTime updated_dt) {
    this.fundid = fundid;
    this.snapshotid = snapshotid;
    this.status = status;
    this.totalcnt = totalcnt;
    this.create_dt = create_dt;
    this.updated_dt = updated_dt;
  }
}
