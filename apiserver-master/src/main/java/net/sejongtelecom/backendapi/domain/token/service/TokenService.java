package net.sejongtelecom.backendapi.domain.token.service;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.dto.FabQueryDto;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import net.sejongtelecom.backendapi.common.entity.WalletTransactionSend;
import net.sejongtelecom.backendapi.common.exception.CResourceNotExistException;
import net.sejongtelecom.backendapi.common.fabric.FabApiConnect;
import net.sejongtelecom.backendapi.common.fabric.sdk.wallet.MakeWallet;
import net.sejongtelecom.backendapi.common.repository.WalletJpaRepo;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.service.ResponseService;
import net.sejongtelecom.backendapi.domain.token.entity.TokenSend;
import org.springframework.stereotype.Service;

/**
 * Version : 1.0
 * 파일명: TokenService.java
 * 작성일자 : 2021-3-5
 * 작성자 : 김주민
 * 설명 : 토큰 API
 * 수정일자 :  2021-3-10
 * 수정자 : 김주민
 * 수정내역 : saveTokenList 작성.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

  private final FabApiConnect fabApiConnect;
  private final ResponseService responseService;
  private final WalletJpaRepo walletJpaRepo;

  /**
   * 설명 : user가 가진 토큰의 수량을 조회한다.
   *
   * @param fundid 조회할 fund의 id
   * @param userid 조회할 지갑 주소
   * @return Map 결과 값을 담은 맵 오브젝트
   */
  public CommonResult queryBalance(String fundid, String userid) {

    String tokenname = convertTokenName(fundid);

    Wallet wallet = walletJpaRepo.findByUserid(userid)
        .orElseThrow(CResourceNotExistException::new);

    String waddress = wallet.getWaddress();

    return balanceOf(tokenname, waddress);
  }

  /**
   * 설명 : 지갑 주소가 가진 토큰의 수량을 조회한다.
   *
   * @param tokenname 조회할 토큰 이름
   * @param waddress  조회할 지갑 주소
   * @return Map 결과 값을 담은 맵 오브젝트
   */
  private CommonResult balanceOf(String tokenname, String waddress) {

    String apiType = "query";

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm(tokenname).funcnm("balanceOf").params(waddress).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540);
    }

    return responseService.getSingleResult(resultMap);
  }

  /**
   * 설명 : 받은 토큰을 소유하고 있는 지갑의 주소와 잔액을 조회한다.
   *
   * @param fundid 조회할 fund의 id
   * @param bookMark  조회할 북마크 값
   * @param pageSize  한번에 조회할 개수
   * @return Map 결과 값을 담은 맵 오브젝트
   */
  public CommonResult balanceList(String fundid, String bookMark, String pageSize) {

    String apiType = "query";

    String tokenname = convertTokenName(fundid);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm(tokenname).funcnm("balanceList")
            .params(bookMark + ";" + pageSize).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540);
    }

    return responseService.getSingleResult(resultMap);
  }

  /**
   * 설명 : 토큰 함수(transfer,mint,burn)를 실행한다. ( 지갑형 트랜잭션 예제용 운영 버전에서는 삭제 필요 )
   *
   * @param tokenname 토큰을 추가 발행할 토큰의 이름
   * @param tokenSend 전송 받을 지갑 주소와 전송할 토큰 량을 저장한 TokenSend 객체
   * @param funcnm 실행할 함수의 이름.
   * @return Map 결과 값을 담은 맵 오브젝트
   */
//  @Deprecated
  public CommonResult transFunc(String tokenname, TokenSend tokenSend, String funcnm) {

    String apiType = "invoke";

    Wallet wallet = walletJpaRepo.findByWaddress(tokenSend.getWaddress())
        .orElseThrow(CResourceNotExistException::new);

    String privateKey = wallet.getPrivatekey();
    String params = tokenSend.getToaddress() + "," + tokenSend.getValue();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    params = MakeWallet.getWalletParams(walletTransactionSend);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm(tokenname).funcnm(funcnm).params(params).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    // vaildcd는 0이면 성공 그 외에는 실패 처리 후 실패 메시지 추가.
    if (!resultMap.get("vaildcd").toString().equals("0")) {
      return responseService.getFailResult(-541);
    }

    return responseService.getSingleResult(resultMap);
  }

  // 전송 데이터 생성.
  public CommonResult transferDummyData(String tokenname, String waddress, int loopCount) {

    HashMap<Object, Object> resultMap = new HashMap<>();

    // 총 데이터 길이
//    int loopCount = Integer.parseInt(loopCountString);

    // 한번에 전송할 길이
    int transactionLen = 30;

    for (int j = 0; j < loopCount; j = j + transactionLen) {

      List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();

      for (int i = j; i < (j + transactionLen); i++) {
        Map<String, Object> map = new HashMap<>();
        map.put("address", "wallet_" + i);
        map.put("amount", 1);
        list_map.add(map);
      }

      Gson gson = new Gson();
      String jsonString = gson.toJson(list_map);

      log.info("JDATA:" + jsonString);

      String apiType = "invoke";

      Wallet wallet = walletJpaRepo.findByWaddress(waddress)
          .orElseThrow(CResourceNotExistException::new);

      String privateKey = wallet.getPrivatekey();
      String params = jsonString;
      WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

      params = MakeWallet.getWalletParams(walletTransactionSend);

      FabQueryDto fabQueryDto =
          FabQueryDto.builder().codenm(tokenname).funcnm("transferMulti").params(params).build();

      resultMap = fabApiConnect.connectFabricAndSetResult(fabQueryDto, apiType);

    }

    return responseService.getSingleResult(resultMap);
  }

  private String convertTokenName(String fundid) {

    if(fundid.equals("dtoken")){
      return fundid;
    }

    return fundid+"_st";
  }
}
