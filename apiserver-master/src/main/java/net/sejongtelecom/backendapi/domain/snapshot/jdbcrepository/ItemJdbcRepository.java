package net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository;

import java.util.List;

/**
 * Version : 1.0
 * 파일명: ItemJdbcRepository.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : Spring DATA JDBC 사용을 위한 레포지토리 인터페이스
 */
public interface ItemJdbcRepository {

  void saveAll(List<ItemJdbc> items);
}
