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
@Table(name = "common_code")
@Proxy(lazy = false)
public class CommonCode extends CommonDateEntity implements Serializable {

  @Id
  @Column(nullable = false, length = 10)
  private String codeid;

  @Column(nullable = false, length = 5)
  private String codegroup;

  @Column(nullable = false, length = 1000)
  private String codename;

  @Column() private int sort;

  @Column(nullable = false, length = 1)
  private String showyn;

  @Column(length = 1000)
  private String desc1;

  @Column(length = 1000)
  private String desc2;

  @Column(length = 1000)
  private String desc3;

  @Builder
  public CommonCode(String codeid, String codename, String codegroup) {
    this.codeid = codeid;
    this.codename = codename;
    this.codegroup = codegroup;
  }
}
