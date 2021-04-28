package net.sejongtelecom.backendapi.domain.token.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private int code;
    private String msg;
    private JSONObject data;

    @Builder
    public TokenDto(boolean success, int code, String msg, JSONObject data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
