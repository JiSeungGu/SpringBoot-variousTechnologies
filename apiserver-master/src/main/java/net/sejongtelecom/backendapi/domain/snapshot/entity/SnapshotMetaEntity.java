package net.sejongtelecom.backendapi.domain.snapshot.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sejongtelecom.backendapi.common.entity.common.CommonDateEntity;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Builder;

/**
 * Version : 1.0
 * 파일명: SnapshotMetaEntity.java
 * 작성일자 : 2021-3-16
 * 작성자 : 한현수
 * 설명 : 스냅샷 메타 엔티티
 */
@Getter
@Setter
@Entity
@Table(name = "snapshotmeta")
@IdClass(SnapshotMetaEntityId.class)
@NoArgsConstructor
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SnapshotMetaEntity extends CommonDateEntity implements Serializable {
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyid;
     */
    @Id
    @Column(name = "fundid")
    private String fundid;

    @Id
    @Column(name = "snapshotid")
    private String snapshotid;

    @Column(name = "status")
    private String status;

    @Column(name = "totalcnt")
    private int totalcnt;

    /*
    //혹시 date 등의 자동이 문제인가 해서 Common 의 데이트를 private 에서 public 으로 바꿔 넣어보기도 했지만.. 안됨
    @Builder
    public SnapshotMetaEntity(String stname, String code, String status, int totalcnt, LocalDateTime create_dt, LocalDateTime updated_dt) {
        this.stname = stname;
        this.code = code;
        this.status = status;
        this.totalcnt = totalcnt;
        this.create_dt = create_dt;
        this.updated_dt = updated_dt;
    }


    @Builder
    public SnapshotMetaEntity(String stname, String code, String status) {
        this.stname = stname;
        this.code = code;
        this.status = status;
    }

    @Builder
    public SnapshotMetaEntity(String stname, String code) {
        this.stname = stname;
        this.code = code;
    }


     */


    public SnapshotMetaEntity setUpdate(String status, int totalcnt) {
        this.status = status;
        this.totalcnt = totalcnt;
        return this;
    }


}
