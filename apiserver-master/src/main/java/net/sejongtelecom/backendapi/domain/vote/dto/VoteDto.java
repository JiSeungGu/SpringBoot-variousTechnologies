package net.sejongtelecom.backendapi.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONObject;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class VoteDto {
    @NotEmpty
    @Size(min=2, max=20)
    private String fundcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String snapshotcode;

    @NotEmpty
    @Size(min=2, max=20)
    private String votecode;

    private String votetitle = "";
    private String votecont1 = "";
    private String votecont2 = "";

    //@DateTimeFormat(pattern = "yyyyMMddHHmmss")
    //@JsonFormat(shape = Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Seoul")
    //@Pattern(regexp = "(1|2)\\d{3}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])")
    private String startdate = "";

    //@DateTimeFormat(pattern = "yyyyMMddHHmmss")
    //@JsonFormat(shape = Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Seoul")
    //@Pattern(regexp = "(1|2)\\d{3}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])")
    private String enddate = "";

    @Builder
    public VoteDto(String fundcode, String snapshotcode, String votecode) {
        this.fundcode = fundcode;
        this.snapshotcode = snapshotcode;
        this.votecode = votecode;
    }
}
