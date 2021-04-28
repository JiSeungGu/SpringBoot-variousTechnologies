package net.sejongtelecom.backendapi.domain.vote.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class VoteActDto {
    @NotEmpty
    @Size(min=2, max=20)
    private String fundcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String snapshotcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String votecode;
    private String userid;
    private String selval;


    @Builder
    public VoteActDto(String fundcode, String snapshotcode, String votecode, String userid) {
        this.fundcode = fundcode;
        this.snapshotcode = snapshotcode;
        this.votecode = votecode;
        this.userid = userid;
    }
}
