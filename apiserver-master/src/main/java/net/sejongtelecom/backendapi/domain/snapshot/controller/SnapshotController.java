package net.sejongtelecom.backendapi.domain.snapshot.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.response.SingleResult;
import net.sejongtelecom.backendapi.common.service.ResponseService;
import net.sejongtelecom.backendapi.domain.snapshot.dto.SnapshotMetaDto;
import net.sejongtelecom.backendapi.domain.snapshot.service.SnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Version : 1.0
 * 파일명: SnapshotController.java
 * 작성일자 : 2021-3-16
 * 작성자 : 한현수
 * 설명 : 스냅샷 생성 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/snapshot")
public class SnapshotController {

    private final SnapshotService snapshotService;
    private final ResponseService responseService;

    // 스냅샷 생성
    @PostMapping(value = "/SNP_0100")
    public CommonResult createSnapshot(@RequestBody @Valid SnapshotMetaDto snapshotMetaDto){
        return snapshotService.createSnapshot(snapshotMetaDto);
    }

    // 스냅샷 조회
    @GetMapping(value = "/SNP_0200")
    public SingleResult<SnapshotMetaDto> getSnapshot(
        @RequestParam(name = "fundid") String fundid,
        @RequestParam(name = "snapshotid") String snapshotid
    ){
        SnapshotMetaDto snapshotMetaDto = SnapshotMetaDto.builder().fundid(fundid).snapshotid(snapshotid).build();
        return responseService.getSingleResult(snapshotService.getSnapshot(snapshotMetaDto));
    }
}