package net.sejongtelecom.backendapi.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.dto.AdminWalletDto;
import net.sejongtelecom.backendapi.common.dto.CdmasterDto;
import net.sejongtelecom.backendapi.common.dto.WalletDto;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.service.WalletService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/wallet")
public class WalletController {

    private final WalletService walletService;

  @PostMapping(value = "/WAL_0100")
  public CommonResult createWallet(@RequestBody WalletDto walletDto) {
    return walletService.createWallet(walletDto);
  }

  @PostMapping(value = "/WAL_0150")
  public CommonResult createAdminWallet(@RequestBody AdminWalletDto adminWalletDto) {
    return walletService.createAdminWallet(adminWalletDto);
  }

  @PostMapping(value = "/WAL_0200")
  public CommonResult reqDeployChainCode(@RequestBody CdmasterDto cdmasterDto) {
    log.info("FUNDID:" + cdmasterDto.getFundid());
    return walletService.reqDeployChainCode(cdmasterDto);
  }


}