package net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Version : 1.0
 * 파일명: ItemJdbc.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : Spring DATA JDBC 사용시 데이터 전달을 위한 DTO
 */
@Getter
@AllArgsConstructor
public class ItemJdbc {
  private Long snapshotseq;
  private String fundid;
  private String snapshotid;
  private String address;
  private int amount;
}