package net.sejongtelecom.backendapi.common.mapper;

import net.sejongtelecom.backendapi.common.dto.AdminWalletDto;
import net.sejongtelecom.backendapi.common.dto.WalletDto;
import net.sejongtelecom.backendapi.common.entity.AdminWallet;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminWalletMapper {
  AdminWalletMapper INSTANCE = Mappers.getMapper(AdminWalletMapper.class);

  public AdminWallet adminWalletDtoToEntity(AdminWalletDto adminWalletDto);

  public AdminWalletDto entityToAdminWallet(AdminWallet adminWallet);
}
