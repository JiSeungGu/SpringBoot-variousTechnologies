package net.sejongtelecom.backendapi.common.mapper;

import net.sejongtelecom.backendapi.common.dto.WalletDto;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {
  WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

  public Wallet walletDtoToEntity(WalletDto walletDto);

  public WalletDto entityToWallet(Wallet wallet);
}
