package net.sejongtelecom.backendapi.domain.vote.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.validation.Valid;
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
import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotEntity;
import net.sejongtelecom.backendapi.domain.snapshot.entity.SnapshotMetaEntity;
import net.sejongtelecom.backendapi.domain.snapshot.repository.SnapshotJpaRepo;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteActDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteActListDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteResultDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

  private final FabApiConnect fabApiConnect;
  private final ResponseService responseService;
  private final WalletJpaRepo walletJpaRepo;
  private final SnapshotJpaRepo snapshotJpaRepo;
  private final String CHAINCODENAME = "vote";
  private final String ADMINADDRESS = "L9jLypTprC58HAM3BDNMXt3CK12k88K6T";


  // 투표 기본 메타 정보 생성
  public CommonResult voteMetaInsert(VoteDto voteDto) {

    String apiType = "invoke";

    Wallet wallet = walletJpaRepo.findByWaddress("BV8oLGiPd8qTGsEqqJ7b9V7X29VuQcea2C")
        .orElseThrow(CResourceNotExistException::new);

    String privateKey = wallet.getPrivatekey();
    //String params = tokenSend.getToaddress() + "," + tokenSend.getValue();

    /*
    long dateStartmillis = 0L;
    long dateEndmillis = 0L;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      Date dateStart = sdf.parse(voteDto.getStartdate().toString());
      Date dateEnd = sdf.parse(voteDto.getEnddate().toString());
      dateStartmillis = dateStart.getTime()/1000;
      dateEndmillis = dateEnd.getTime()/1000;

    } catch(Exception e) {

    }

    String params = voteDto.getFundcode() + "," + voteDto.getVotecode() + "," + voteDto
        .getVotetitle() + "," + voteDto.getVotecont1() + "," + voteDto.getVotecont2()
        + "," + dateStartmillis + "," + dateEndmillis;

     */
    String params = voteDto.getFundcode() + "," + voteDto.getSnapshotcode() + "," + voteDto.getVotecode() + "," + voteDto
        .getVotetitle() + "," + voteDto.getVotecont1() + "," + voteDto.getVotecont2()
        + "," + voteDto.getStartdate() + "," + voteDto.getEnddate();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    log.info("params = " + params);
    log.info("params2 = " + voteDto.toString());
    params = MakeWallet.getWalletParams(walletTransactionSend);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteMetaInsert").params(params).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    log.info("resultMap = " + resultMap.toString());
    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    // vaildcd는 0이면 성공 그 외에는 실패 처리 후 실패 메시지 추가.
    if (!resultMap.get("vaildcd").toString().equals("0")) {
      return responseService.getFailResult(-541, resultMap.get("validmsg").toString());
    }

    return responseService.getSingleResult(resultMap);
  }


  // 투표 기본 메타 정보 조회
  public CommonResult voteMetaInfo(VoteDto voteDto) {

    log.info("votedto" + voteDto.toString());
    String apiType = "query";

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteMetaInfo").params(voteDto.getFundcode() + ";" + voteDto.getSnapshotcode() + ";" + voteDto.getVotecode()).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }

    return responseService.getSingleResult(resultMap);

  }

  // 투표 기본 메타 정보 수정
  public CommonResult voteMetaModify(VoteDto voteDto) {

    String apiType = "invoke";

    Wallet wallet = walletJpaRepo.findByWaddress("BV8oLGiPd8qTGsEqqJ7b9V7X29VuQcea2C")
        .orElseThrow(CResourceNotExistException::new);

    String privateKey = wallet.getPrivatekey();

    String params = voteDto.getFundcode() + "," + voteDto.getSnapshotcode() + "," + voteDto.getVotecode() + "," + voteDto
        .getVotetitle() + "," + voteDto.getVotecont1() + "," + voteDto.getVotecont2()
        + "," + voteDto.getStartdate() + "," + voteDto.getEnddate();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    log.info("params = " + params);
    log.info("params2 = " + voteDto.toString());
    params = MakeWallet.getWalletParams(walletTransactionSend);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteMetaModify").params(params).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    // vaildcd는 0이면 성공 그 외에는 실패 처리 후 실패 메시지 추가.
    if (!resultMap.get("vaildcd").toString().equals("0")) {
      return responseService.getFailResult(-541, resultMap.get("validmsg").toString());
    }

    return responseService.getSingleResult(resultMap);

  }


  // 투표 하기
  public CommonResult vote(VoteActDto voteActDto) {

    String apiType = "invoke";

    Wallet wallet = walletJpaRepo.findByUserid(voteActDto.getUserid())
        .orElseThrow(CResourceNotExistException::new);

    SnapshotEntity snapshotEntity = snapshotJpaRepo.findByStnameAndCodeAndAddress("dtoken", voteActDto.getSnapshotcode(), wallet.getWaddress());
    //SnapshotEntity snapshotEntity = snapshotJpaRepo.findByStnameAndCodeAndAddress(voteActDto.getFundcode(), voteActDto.getSnapshotcode(), wallet.getWaddress());

    if (snapshotEntity == null) {
      // TODO error code
      return responseService.getFailResult(-540, "돈 없음 ");
    }

    String privateKey = wallet.getPrivatekey();

    //String params = voteActDto.getFundcode() + "," + voteActDto.getSnapshotcode() + "," + voteActDto.getVotecode() + "," + voteActDto.getSelval() + "," + snapshotEntity.getAmount();
    String params = "fund10" + "," + voteActDto.getSnapshotcode() + "," + voteActDto.getVotecode() + "," + voteActDto.getSelval() + "," + snapshotEntity.getAmount();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    log.info("params = " + params);
    log.info("params2 = " + voteActDto.toString());
    params = MakeWallet.getWalletParams(walletTransactionSend);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("vote").params(params).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    log.info("resultMap = " + resultMap.toString());
    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    // vaildcd는 0이면 성공 그 외에는 실패 처리 후 실패 메시지 추가.
    if (!resultMap.get("vaildcd").toString().equals("0")) {
      return responseService.getFailResult(-541, resultMap.get("vaildmsg").toString());
    }
    return responseService.getSingleResult(resultMap);

  }


  // 투표 정보 조회
  public CommonResult voteInfo(VoteActDto voteActDto) {

    log.info("votedto" + voteActDto.toString());
    String apiType = "query";

    Wallet wallet = walletJpaRepo.findByUserid(voteActDto.getUserid())
        .orElseThrow(CResourceNotExistException::new);

    // 조회는 모두 지갑 안 쓰기로
    /*
    Wallet wallet = walletJpaRepo.findByUserid(voteActDto.getUserid())
        .orElseThrow(CResourceNotExistException::new);

    log.info("addr = " + wallet.getWaddress());
    String privateKey = wallet.getPrivatekey();

    String params = voteActDto.getFundcode() + "," + voteActDto.getSnapshotcode() + "," + voteActDto.getVotecode();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    log.info("params = " + params);
    log.info("params2 = " + voteActDto.toString());
    params = MakeWallet.getWalletParams(walletTransactionSend);
     */

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteInfo").params(wallet.getWaddress() + ";" + voteActDto.getFundcode() + ";" + voteActDto.getSnapshotcode() + ";" + voteActDto.getVotecode()).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }

    return responseService.getSingleResult(resultMap);


  }


  // 투표자 목록 조회
  public CommonResult voteParticipantsList(VoteActListDto voteActListDto) {
    log.info("votedto" + voteActListDto.toString());
    String apiType = "query";

    /*
    String privateKey = wallet.getPrivatekey();

    String params = voteActListDto.getFundcode() + ";" + voteActListDto.getSnapshotcode() + ";" + voteActListDto.getVotecode() + ";" + voteActListDto.getBookmark() + ";" + voteActListDto.getSelstatus();
    WalletTransactionSend walletTransactionSend = new WalletTransactionSend(privateKey, params);

    log.info("params = " + params);
    //log.info("params2 = " + voteActDto.toString());
    params = MakeWallet.getWalletParams(walletTransactionSend);

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteParticipantsList").params(params).build();

     */

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteParticipantsList").params(voteActListDto.getFundcode() + ";" + voteActListDto.getSnapshotcode() + ";" + voteActListDto.getVotecode() + ";" + voteActListDto.getBookmark() + ";" + voteActListDto.getSelstatus() + ";" + voteActListDto.getCntperpage()).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    return responseService.getSingleResult(resultMap);
  }


  // 투표 결과 조회
  public CommonResult voteResult(VoteResultDto voteResultDto) {

    log.info("votedto" + voteResultDto.toString());
    String apiType = "query";

    FabQueryDto fabQueryDto =
        FabQueryDto.builder().codenm("vote").funcnm("voteResult").params(voteResultDto.getFundcode() + ";" + voteResultDto.getSnapshotcode() + ";" + voteResultDto.getVotecode()).build();

    HashMap<Object, Object> resultMap = fabApiConnect
        .connectFabricAndSetResult(fabQueryDto, apiType);

    // rescd가 200이면 성공
    if (!resultMap.get("rescd").toString().equals("200")) {
      return responseService.getFailResult(-540, resultMap.get("resmsg").toString());
    }
    return responseService.getSingleResult(resultMap);

  }
}
