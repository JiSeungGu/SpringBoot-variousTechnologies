package net.sejongtelecom.backendapi.domain.snapshot.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Version : 1.0
 * 파일명: SnapshotMetaEntityId.java
 * 작성일자 : 2021-3-16
 * 작성자 : 한현수
 * 설명 : SnapshotMetaEntity 의 식별자 클래스
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SnapshotMetaEntityId implements Serializable {

    private String fundid;

    private String snapshotid;

    public SnapshotMetaEntityId(String fundid, String snapshotid) {
        this.fundid = fundid;
        this.snapshotid = snapshotid;
    }
}
