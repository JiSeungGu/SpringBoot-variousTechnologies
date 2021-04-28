package net.sejongtelecom.backendapi.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminWalletDto {

  @JsonProperty(value = "fundid")
  private String fundid;

  @JsonProperty(value = "waddress")
  private String waddress;

  @JsonProperty(value = "privatekey")
  private String privatekey;

  public void setPrivatekey(String privatekey){
    this.privatekey = privatekey;
  }

  public void setWaddress(String waddress){
    this.waddress = waddress;
  }

  @Builder
  public AdminWalletDto(String fundid, String waddress, String privatekey) {
    this.fundid = fundid;
    this.waddress = waddress;
    this.privatekey = privatekey;
  }
}
