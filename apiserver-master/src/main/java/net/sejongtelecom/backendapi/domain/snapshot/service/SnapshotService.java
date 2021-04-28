package net.sejongtelecom.backendapi.domain.snapshot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.exception.CResourceNotExistException;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.response.SingleResult;
import net.sejongtelecom.backendapi.common.service.ResponseService;
import net.sejongtelecom.backendapi.domain.snapshot.dto.SnapshotMetaDto;
import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotMetaEntity;
import net.sejongtelecom.backendapi.domain.snapshot.mapper.SnapshotMetaMapper;
import net.sejongtelecom.backendapi.domain.snapshot.jdbcrepository.ItemJdbc;
import net.sejongtelecom.backendapi.domain.snapshot.repository.SnapshotMetaJpaRepo;
import net.sejongtelecom.backendapi.domain.token.service.TokenService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * Version : 1.0
 * 파일명: SnapshotService.java
 * 작성일자 : 2021-3-17
 * 작성자 : 한현수
 * 설명 : 스냅샷 생성 API 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotService {
  private final ResponseService responseService;
  private final SnapshotMetaJpaRepo snapshotMetaJpaRepo;
  private final TokenService tokenService;
  private final ItemJdbcService itemJdbcService;

  // 블록체인에서 한번에 조회할 페이지 사이즈 (st 목록 조회시)
  @Value("${app.vote.stlist-pagesize}")
  String pageSize;

  @Value("${app.delimiter.st}")
  String STDELIIETER;

  /**
   * 설명 : 스냅샷을 생성한다.
   * @param `stname` 스냅샷을 생성할 토큰 이름
   * @param `code` 스냅샷을 생성할 구분자 (보통 날짜)
   * @return CommonResult
   */
  public CommonResult createSnapshot(SnapshotMetaDto snapshotMetaDto) {

    HashMap<Object, Object> resultMap = new HashMap<>();  //리턴 결과 담을 맵
    String fundid = snapshotMetaDto.getFundid();
    String stname = fundid + STDELIIETER;
    String snapshotid = snapshotMetaDto.getSnapshotid();

    // 기 등록된 스냅샷인지 체크
    long countVoteMeta = snapshotMetaJpaRepo.countByFundidAndSnapshotid(fundid, snapshotid);
    if (countVoteMeta > 0) {
      // code -570 : 이미 등록된 또는 등록중인 스냅샷입니다.
      return responseService.getFailResult(-570);
    }
    // --기 등록된 스냅샷인지 체크

    // 스냅샷 메타 정보 insert
    log.info("snapshotMetaDto : " + snapshotMetaDto.getSnapshotid());
    log.info("snapshotMetaDto : " + snapshotMetaDto.getFundid());

    SnapshotMetaEntity snapshotMetaEntity = SnapshotMetaEntity.builder().fundid(fundid).snapshotid(snapshotid).status("0").build();
    log.info("snapshotMetaDto : " + snapshotMetaEntity.getSnapshotid());
    log.info("snapshotMetaDto : " + snapshotMetaEntity.getFundid());

    // mapstruct 사용 테스트 를 위한 코드
    SnapshotMetaEntity VME_ORG = SnapshotMetaMapper.INSTANCE.snapshotMetaDtoToEntity(snapshotMetaDto);
    log.info("snapshotMetaDto : " + VME_ORG.getSnapshotid());
    log.info("snapshotMetaDto : " + VME_ORG.getFundid());

    snapshotMetaJpaRepo.save(snapshotMetaEntity);
    // --스냅샷 메타 정보 insert

    // 블록체인 조회
    String bookmark = ""; //북마크 저장
    boolean isEnd = false;  // 블록체인 페이징 중 마지막인지 구분 값 (블록체인 컨트랙트 제공)
    List<ItemJdbc> items = new ArrayList<>(); // jdbc 로 insert 할 항목들 (블록체인에서 받아온 데이터 저장)

    // 속도 측정을 위한 스탑와치 생성
    StopWatch stopWatch = new StopWatch("SNAPSHOT 속도 체크");
    stopWatch.start("블록체인 데이터 query 및 메모리 저장");

    //북마크 기준으로 데이터 끝까지 블록체인에서 받아옴
    while (true) {
      SingleResult CR = (SingleResult) tokenService.balanceList(stname, bookmark, pageSize);

      HashMap<Object, Object> fabric_result = (HashMap<Object, Object>) CR.getData();
      String fabric_result_str = fabric_result.get("value").toString();

      try {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(fabric_result_str);
        bookmark = (String) object.get("bookmark");
        isEnd = (boolean) object.get("isEnd");
        JSONArray arr = (JSONArray) object.get("balanceList");

        for (int i = 0; i < arr.size(); i++) {
          JSONObject tmp = (JSONObject) arr.get(i);
          Map<String, Object> map = null;
          map = new ObjectMapper().readValue(tmp.toJSONString(), Map.class);
          //aList.add(map);
          items.add(new ItemJdbc(null, fundid, snapshotid, map.get("address").toString(), Integer.parseInt(map.get("amount").toString())));
        }
        if (isEnd) {
          break;
        }
      } catch (Exception e) {
        log.error(e.toString());
      }
    }
    stopWatch.stop();
    // --블록체인 조회

    stopWatch.start("JPA 이용 데이터베이스 저장");
    /*
    // JPA 이용 저장 테스트 코드
    for (int k=0; k < aList.size(); k++) {
      //log.info(aList.get(k).toString());
      SnapshotEntity SSD = SnapshotEntity.builder()
          .address(((HashMap)aList.get(k)).get("address").toString())
          .amount(((HashMap<String, Integer>)aList.get(k)).get("amount")).build();
      snapshotJpaRepo.save(SSD);
      //SnapshotEntity SSE = new ObjectMapper().readValue.readValue(aList.get(k).toString(), SnapshotEntity.class);
    }*/
    stopWatch.stop();

    log.info("total size = " + items.size());

    stopWatch.start("JDBC 이용 데이터베이스 저장");
    itemJdbcService.saveAll(items);
    stopWatch.stop();
    log.info(" snapshot time : " + stopWatch.prettyPrint());
    log.info(" total mil : " + stopWatch.getTotalTimeMillis());
    log.info(" total sec : " + stopWatch.getTotalTimeSeconds());
    //long endTime = System.currentTimeMillis();
    //log.info("OOO Elapsed: {} secs", ((endTime - startTime) / 1000.0f));
    /*
    SnapshotMetaEntity SS = snapshotMetaJpaRepo.findByStnameAndCode(stname, snapshotMetaDto.getCode());
    log.info(SS.getStname());
    log.info(SS.getCode());
    SS.setUpdate("1");
    snapshotMetaJpaRepo.save(SS);
     */
    // 스냅샷 생성을 완료로 업데이트 한다. 추가로, 스냅샷에 존재하는 계정 수도 업데이트
    snapshotMetaEntity.setUpdate("1", items.size());
    snapshotMetaJpaRepo.save(snapshotMetaEntity);

    return responseService.getSingleResult(resultMap);
  }

  /**
   * 설명 : 지정한 스냅샷을 조회한다.
   * @param `stname` 스냅샷을 생성할 토큰 이름
   * @param `code` 스냅샷을 생성할 구분자 (보통 날짜)
   * @return SnapshotMetaDto 스냅샷 정보 (생성일, 현재 상태, 총 건수 등)
   */
  public SnapshotMetaDto getSnapshot(SnapshotMetaDto snapshotMetaDto) {
    SnapshotMetaEntity snapshotMetaEntity = Optional.ofNullable(snapshotMetaJpaRepo.findByFundidAndSnapshotid(snapshotMetaDto.getFundid(), snapshotMetaDto.getSnapshotid())).orElseThrow(CResourceNotExistException::new);
    SnapshotMetaDto snapshotMetaDtoResult = SnapshotMetaMapper.INSTANCE.entityToSnapshot(snapshotMetaEntity);
    return snapshotMetaDtoResult;
  }
}
