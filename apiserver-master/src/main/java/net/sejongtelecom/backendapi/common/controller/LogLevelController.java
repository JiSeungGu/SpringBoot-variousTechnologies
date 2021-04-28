package net.sejongtelecom.backendapi.common.controller;

import lombok.RequiredArgsConstructor;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.service.HandleLogLevelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/log")
public class LogLevelController {

    private final HandleLogLevelService handleLogLevelService;

    // log level 입력 필요 -> TRACE  <  DEBUG  <  INFO  <  WARN  <  ERROR
    @GetMapping(value = "/level/{loglevel}")
    public CommonResult changeLoggerLevel(@PathVariable("loglevel") String loggerLevel) {
        return handleLogLevelService.setLogLevel(loggerLevel);
    }

    @GetMapping(value = "/level")
    public CommonResult getLoggerLevel() {
        return handleLogLevelService.getLogLevel();
    }
}
