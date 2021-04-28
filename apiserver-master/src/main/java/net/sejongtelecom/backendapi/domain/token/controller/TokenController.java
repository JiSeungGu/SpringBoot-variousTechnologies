package net.sejongtelecom.backendapi.domain.token.controller;

import lombok.RequiredArgsConstructor;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.domain.token.entity.TokenSend;
import net.sejongtelecom.backendapi.domain.token.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Version : 1.0
 * 파일명: TokenController.java
 * 작성일자 : 2021-3-5
 * 작성자 : 김주민
 * 설명 : 토큰 API uri를 매핑한 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/token")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping(value = "/TOK_0100")
    public CommonResult queryBalance(
        @RequestParam(name = "fundid") String fundid,
        @RequestParam(name = "userid") String userid
    ){
        return tokenService.queryBalance(fundid, userid);
    }

    @GetMapping(value = "/TOK_0200")
    public CommonResult balanceList(
        @RequestParam(name = "fundid") String fundid,
        @RequestParam(name = "book_mark") String bookMark,
        @RequestParam(name = "page_size") String pageSize
    ){
        return tokenService.balanceList(fundid, bookMark, pageSize);
    }

    @PostMapping(value = "/mint/{tokenname}")
    public CommonResult mint(
        @PathVariable(name = "tokenname") String tokenname,
        @RequestBody TokenSend tokenSend
    ){
        return tokenService.transFunc(tokenname, tokenSend, "mint");
    }

    @PostMapping(value = "/burn/{tokenname}")
    public CommonResult burn(
        @PathVariable(name = "tokenname") String tokenname,
        @RequestBody TokenSend tokenSend
    ){
        return tokenService.transFunc(tokenname, tokenSend, "burn");
    }

    @PostMapping(value = "/transfer_dummy/{tokenname}")
    public CommonResult transferDummy(
        @PathVariable(name = "tokenname") String tokenname,
        @RequestBody TokenSend tokenSend
    ){
        return tokenService.transferDummyData(tokenname, tokenSend.getWaddress(), tokenSend.getValue());
    }
}