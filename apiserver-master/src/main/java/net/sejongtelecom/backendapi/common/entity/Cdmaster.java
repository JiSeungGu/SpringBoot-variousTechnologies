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
@Table(name = "cdmaster")
@Proxy(lazy = false)
public class Cdmaster extends  CommonDateEntity implements Serializable {

  @Id
  @Column(nullable = false, length=50)
  private String fundid;

  @Column(nullable = false,length = 100)
  private int initcnt;

  @Column(nullable = false,length = 1)
  private String iv;

  @Column(nullable = false,length = 1)
  private String mo;

  @Column(nullable = false,length = 1)
  private String st;

  @Column(nullable = false,length = 1)
  private String tr;

  @Column(nullable = false,length = 1)
  private String dv;

  @Column(nullable = false,length = 1)
  private String stat;

  @Builder
  public Cdmaster(String fundid, int initcnt) {
    this.fundid = fundid;
    this.initcnt = initcnt;
    this.iv = "N";
    this.mo = "N";
    this.st = "N";
    this.tr = "N";
    this.dv = "N";
    this.stat = "0";
  }
}
