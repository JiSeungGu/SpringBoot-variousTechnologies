package net.sejongtelecom.backendapi.domain.vote.controller;
import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.response.SingleResult;

import net.sejongtelecom.backendapi.common.service.ResponseService;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteActDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteActListDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteDto;
import net.sejongtelecom.backendapi.domain.vote.dto.VoteResultDto;
import net.sejongtelecom.backendapi.domain.vote.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/vote")
public class VoteController {

  private final ResponseService responseService;
  private final VoteService voteService;
  private static ValidatorFactory factory;
  private static Validator validator;

  //  @ApiImplicitParams({
//      @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//  })
  //@ApiOperation(value = "투표 기본 메타 정보 생성", notes = "(권한) 관리자 전용 API <br />(지갑로직적용) Y <br />(이벤트) N <br />(내용) 투표 기본 메타 정보 및 투표 대상자에 대한 목록을 펀드에서 받아와서(SnapShot) 저장해 놓는다.")
  @PostMapping(value = "/votemeta")
  public CommonResult voteMetaInsert(@RequestBody @Valid VoteDto voteDto) {
    return voteService.voteMetaInsert(voteDto);
  }

  //  @ApiImplicitParams({
//      @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//  })
  //@ApiOperation(value = "투표 기본 메타 정보", notes = "(권한) 누구나 <br />(지갑로직적용) N <br />(이벤트) N <br />(내용) 기본 투표 메타 정보 가져오기")
  @GetMapping(value = "/votemeta")
  public CommonResult voteMetaInfo(
      @RequestParam(name = "fundcode") String fundcode,
      @RequestParam(name = "snapshotcode") String snapshotcode,
      @RequestParam(name = "votecode") String votecode
      ) {
    VoteDto voteDto = VoteDto.builder().fundcode(fundcode).votecode(votecode).snapshotcode(snapshotcode).build();

    return voteService.voteMetaInfo(voteDto);
  }

  //@ApiOperation(value = "투표 기본 메타 정보 수정", notes = "(권한) 관리자 전용 API <br />(지갑로직적용) Y <br />(이벤트) N <br />(내용) 투표에 대한 메타데이터 수정 (가능항목:투표제목, 투표내용1, 투표내용2, 합의 비율)")
  @PutMapping(value = "/votemeta")
  public CommonResult voteMetaModify(@Valid @RequestBody VoteDto voteDto) {
    return voteService.voteMetaModify(voteDto);
  }

  //@ApiOperation(value = "투표 하기", notes = "(권한) 초기 투표 생성시 토큰을 가지고 있는 계정만 가능 <br />(지갑로직적용) Y <br />(이벤트) N <br />(내용) 투표한 지갑정보를 이용하여 해당 지갑이 소유한 ST 토큰 수량만큼 투표")
  @PostMapping(value = "/vote")
  public CommonResult vote(@RequestBody @Valid VoteActDto voteActDto) {
    return voteService.vote(voteActDto);
  }

  //@ApiOperation(value = "본인 투표 확인", notes = "(권한) 초기 투표 생성시 토큰을 가지고 있는 계정만 가능 <br />(지갑로직적용) Y <br />(이벤트) N <br />(내용) 본인이 투표한 내용 확인")
  @GetMapping(value = "/vote")
  public CommonResult voteInfo(
      @RequestParam(name = "fundcode") String fundcode,
      @RequestParam(name = "snapshotcode") String snapshotcode,
      @RequestParam(name = "votecode") String votecode,
      @RequestParam(name = "userid") String userid) {
    VoteActDto voteActDto = VoteActDto.builder().fundcode(fundcode).snapshotcode(snapshotcode).votecode(votecode).userid(userid).build();
    return voteService.voteInfo(voteActDto);
  }

  //@ApiOperation(value = "투표 참여자 목록", notes = "(권한) 관리자 전용 API <br />(지갑로직적용) Y <br />(이벤트) N <br />(내용) 투표 참여자 목록 가져오기. 북마크는 초기 목록 요청시에는 빈값. <br />다음페이지 요청시는 필수로 넣어야 함. <br />selstatus(구분) : 1 (모든목록), 2 (투표 참여자), 3 (투표 미참여자)")
  @GetMapping(value = "/voteParticipantsList")
  public CommonResult voteParticipantsList(
      @RequestParam String fundcode,
      @RequestParam String snapshotcode,
      @RequestParam String votecode,
      @RequestParam(defaultValue = "") String bookmark,
      @RequestParam(defaultValue = "1") String selstatus,
      @RequestParam(defaultValue = "20") String cntperpage
      ) {

    VoteActListDto voteActListDto = VoteActListDto.builder().fundcode(fundcode).snapshotcode(snapshotcode).votecode(votecode).bookmark(bookmark).selstatus(selstatus).cntperpage(cntperpage).build();
    return voteService.voteParticipantsList(voteActListDto);
  }

  //@ApiOperation(value = "투표 결과 조회", notes = "(권한) 누구나 <br />(지갑로직적용) N <br />(이벤트) N <br />(내용) 투표 결과를 조회하는 기능")
  @GetMapping(value = "/voteResult")
  public CommonResult voteResult(
      @RequestParam String fundcode,
      @RequestParam String snapshotcode,
      @RequestParam String votecode
  ) {

    VoteResultDto voteResultDto = VoteResultDto.builder().fundcode(fundcode).snapshotcode(snapshotcode).votecode(votecode).build();
    return voteService.voteResult(voteResultDto);
  }
}
