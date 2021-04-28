package net.sejongtelecom.backendapi.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WalletTransactionSend {

  private String privatekey;
  private String params;

}
