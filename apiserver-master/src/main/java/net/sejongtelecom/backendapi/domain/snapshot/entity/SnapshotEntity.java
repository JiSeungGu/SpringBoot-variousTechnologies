package net.sejongtelecom.backendapi.domain.snapshot.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.sejongtelecom.backendapi.common.entity.common.CommonDateEntity;
import org.hibernate.annotations.Proxy;

/**
 * Version : 1.0
 * 파일명: SnapshotEntity.java
 * 작성일자 : 2021-3-16
 * 작성자 : 한현수
 * 설명 : 스냅샷 실제 데이터 엔티티
 */
@Getter
@Setter
@Entity
@Table(name = "snapshot")
@Proxy(lazy = false)
public class SnapshotEntity extends CommonDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapshotseq;

    /*
    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "stname", referencedColumnName = "stname"),
        @JoinColumn(name = "code", referencedColumnName = "code")
    })
    private SnapshotMetaEntity snapshotMetaEntity;
*/
    @Column(name = "fundid")
    private String fundid;

    @Column(name = "snapshotid")
    private String snapshotid;


    @Column(name = "address")
    private String address;

    @Column(name = "amount")
    private int amount;


    /*
    @Builder
    public SnapshotEntity(String tokenname, String task, String code, String address, int amount) {
        this.tokenname = tokenname;
        this.task = task;
        this.code = code;
        this.address = address;
        this.amount = amount;
        // this.createdAt = LocalDateTime.now();
    }

     */
}
