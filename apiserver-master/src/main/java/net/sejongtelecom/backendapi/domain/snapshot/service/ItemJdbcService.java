package net.sejongtelecom.backendapi.domain.snapshot.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository.ItemJdbc;
import net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository.ItemJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Version : 1.0
 * 파일명: ItemJdbcService.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : Spring DATA JDBC 사용을 위한 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemJdbcService {

  private final ItemJdbcRepository repository;

  public void saveAll(List<ItemJdbc> items) {
    repository.saveAll(items);
  }
}
