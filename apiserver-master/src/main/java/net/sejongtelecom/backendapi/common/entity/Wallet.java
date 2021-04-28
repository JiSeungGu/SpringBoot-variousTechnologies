package net.sejongtelecom.backendapi.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.sejongtelecom.backendapi.common.entity.common.CommonDateEntity;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "userwallet")
@Proxy(lazy = false)
public class Wallet extends  CommonDateEntity implements Serializable {

  @Id
  @Column(nullable = false, length = 50)
  private String userid;

  @Column(length = 300)
  private String waddress;

  @Column(length = 500)
  private String privatekey;

  @Builder
  public Wallet(String userid, String waddress, String privatekey) {
    this.userid = userid;
    this.waddress = waddress;
    this.privatekey = privatekey;
    // this.createdAt = LocalDateTime.now();
  }
}
