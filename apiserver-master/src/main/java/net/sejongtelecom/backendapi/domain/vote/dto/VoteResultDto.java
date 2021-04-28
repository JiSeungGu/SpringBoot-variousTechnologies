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
public class VoteResultDto {
    @NotEmpty
    @Size(min=2, max=20)
    private String fundcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String snapshotcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String votecode;

    @Builder
    public VoteResultDto(String fundcode, String snapshotcode, String votecode) {
        this.fundcode = fundcode;
        this.snapshotcode = snapshotcode;
        this.votecode = votecode;
    }
}
