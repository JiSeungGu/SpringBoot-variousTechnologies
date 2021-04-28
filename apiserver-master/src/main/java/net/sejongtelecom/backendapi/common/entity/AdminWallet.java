package net.sejongtelecom.backendapi.common.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.sejongtelecom.backendapi.common.entity.common.CommonDateEntity;
import org.hibernate.annotations.Proxy;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "adminwallet")
@Proxy(lazy = false)
public class AdminWallet extends  CommonDateEntity implements Serializable {

  @Id
  @Column(nullable = false, length = 50)
  private String fundid;

  @Column(length = 300)
  private String waddress;

  @Column(length = 500)
  private String privatekey;

  @Builder
  public AdminWallet(String fundid, String waddress, String privatekey) {
    this.fundid = fundid;
    this.waddress = waddress;
    this.privatekey = privatekey;
    // this.createdAt = LocalDateTime.now();
  }
}
