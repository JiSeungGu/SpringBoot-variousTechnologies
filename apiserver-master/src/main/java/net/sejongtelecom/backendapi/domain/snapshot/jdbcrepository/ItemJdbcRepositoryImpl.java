package net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Version : 1.0
 * 파일명: ItemJdbcRepositoryImpl.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : Spring DATA JDBC 사용을 위한 레포지토리 인터페이스 구현 클래스
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemJdbcRepositoryImpl implements ItemJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  @Value("${app.snapshot.batchinsert-count}")
  private int batchSize = 10000;

  @Override
  public void saveAll(List<ItemJdbc> items) {
    int batchCount = 0;
    List<ItemJdbc> subItems = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
      subItems.add(items.get(i));
      if ((i + 1) % batchSize == 0) {
        batchCount = batchInsert(batchSize, batchCount, subItems);
      }
    }
    if (!subItems.isEmpty()) {
      batchCount = batchInsert(batchSize, batchCount, subItems);
    }
    log.info("batchCount: " + batchCount);
  }

  private int batchInsert(int batchSize, int batchCount, List<ItemJdbc> subItems) {
    jdbcTemplate.batchUpdate("insert into snapshot (`fundid`, `snapshotid`, `address`, `amount`, `create_dt`) values (?, ?, ?, ?, now())",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, subItems.get(i).getFundid());
            ps.setString(2, subItems.get(i).getSnapshotid());
            ps.setString(3, subItems.get(i).getAddress());
            ps.setInt(4, subItems.get(i).getAmount());
          }
          @Override
          public int getBatchSize() {
            return subItems.size();
          }
        });
    subItems.clear();
    batchCount++;
    return batchCount;
  }
}