package net.sejongtelecom.backendapi.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.dto.AdminWalletDto;
import net.sejongtelecom.backendapi.common.dto.CdmasterDto;
import net.sejongtelecom.backendapi.common.dto.WalletDto;
import net.sejongtelecom.backendapi.common.entity.AdminWallet;
import net.sejongtelecom.backendapi.common.entity.Cdmaster;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import net.sejongtelecom.backendapi.common.fabric.sdk.wallet.MakeWallet;
import net.sejongtelecom.backendapi.common.mapper.AdminWalletMapper;
import net.sejongtelecom.backendapi.common.mapper.WalletMapper;
import net.sejongtelecom.backendapi.common.repository.AdminWalletJpaRepo;
import net.sejongtelecom.backendapi.common.repository.CdmasterJpaRepo;
import net.sejongtelecom.backendapi.common.repository.WalletJpaRepo;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

  // private final MakeWallet makeWallet;
  private final ResponseService responseService;
  private final WalletJpaRepo walletJpaRepo;
  private final AdminWalletJpaRepo adminWalletJpaRepo;
  private final CdmasterJpaRepo cdmasterJpaRepo;

  // 지갑 생성
  public CommonResult createWallet(WalletDto walletDto) {

    walletDto = MakeWallet.makeNewPrivateKey(walletDto);

    long walletCount = walletJpaRepo.countByUserid(walletDto.getUserid());

    if (walletCount > 0) {
        return responseService.getFailResult(-530);
    }

    Wallet wallet = WalletMapper.INSTANCE.walletDtoToEntity(walletDto);
    walletJpaRepo.save(wallet);

    return responseService.getSingleResult(null);
  }

  // 펀드 관리자 지갑 생성
  public CommonResult createAdminWallet(AdminWalletDto adminWalletDto) {

    WalletDto dummyWalletDto = WalletDto.builder().build();

    dummyWalletDto = MakeWallet.makeNewPrivateKey(dummyWalletDto);

    long adminWalletCount = adminWalletJpaRepo.countByFundid(adminWalletDto.getFundid());

    if (adminWalletCount > 0) {
      return responseService.getFailResult(-532);
    }

    adminWalletDto.setPrivatekey(dummyWalletDto.getPrivatekey());
    adminWalletDto.setWaddress(dummyWalletDto.getWaddress());

    AdminWallet adminWallet = AdminWalletMapper.INSTANCE.adminWalletDtoToEntity(adminWalletDto);
    adminWalletJpaRepo.save(adminWallet);

    return responseService.getSingleResult(null);
  }

  // 온라인 체인코드 배포 요청
  public CommonResult reqDeployChainCode(CdmasterDto cdmasterDto) {

    long cdMasterCount = cdmasterJpaRepo.countByFundid(cdmasterDto.getFundid());

    if (cdMasterCount > 0) {
      return responseService.getFailResult(-531);
    }

    Cdmaster cdmaster = Cdmaster.builder().fundid(cdmasterDto.getFundid()).initcnt(cdmasterDto.getInitcnt()).build();
    cdmasterJpaRepo.save(cdmaster);

    return responseService.getSingleResult(null);
  }
}
