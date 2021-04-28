package net.sejongtelecom.backendapi.domain.token.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenSend {
  private String waddress;
  private String toaddress;
  private int value;

  @Builder
  public TokenSend(String waddress, String toaddress, int value) {
    this.waddress = waddress;
    this.toaddress = toaddress;
    this.value = value;
  }

}
