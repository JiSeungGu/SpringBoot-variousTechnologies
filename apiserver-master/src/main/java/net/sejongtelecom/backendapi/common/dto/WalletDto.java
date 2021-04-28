package net.sejongtelecom.backendapi.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WalletDto {

  @JsonProperty(value = "userid")
  private String userid;

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
  public WalletDto(String userid, String waddress, String privatekey) {
    this.userid = userid;
    this.waddress = waddress;
    this.privatekey = privatekey;
  }
}
