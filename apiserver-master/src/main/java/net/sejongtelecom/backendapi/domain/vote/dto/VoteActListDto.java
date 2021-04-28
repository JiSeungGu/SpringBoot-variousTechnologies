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
public class VoteActListDto {
    @NotEmpty
    @Size(min=2, max=20)
    private String fundcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String snapshotcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String votecode;
    private String bookmark;
    private String selstatus;
    private String cntperpage;

    @Builder
    public VoteActListDto(String fundcode, String snapshotcode, String votecode, String bookmark, String selstatus, String cntperpage) {
        this.fundcode = fundcode;
        this.snapshotcode = snapshotcode;
        this.votecode = votecode;
        this.bookmark = bookmark;
        this.selstatus = selstatus;
        this.cntperpage = cntperpage;
    }
}
