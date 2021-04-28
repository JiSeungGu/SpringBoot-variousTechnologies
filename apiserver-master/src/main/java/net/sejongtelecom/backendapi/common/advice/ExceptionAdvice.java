package net.sejongtelecom.backendapi.common.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.exception.CResourceNotExistException;
import net.sejongtelecom.backendapi.common.exception.InsertDataException;
import net.sejongtelecom.backendapi.common.exception.SelectDataException;
import net.sejongtelecom.backendapi.common.response.CommonResult;
import net.sejongtelecom.backendapi.common.service.ResponseService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.error(e.toString());
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    // 데이터베이스 인서트 에러
    @ExceptionHandler(InsertDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult insertDataException(HttpServletRequest request, InsertDataException e) {
        log.error(e.toString());
        return responseService.getFailResult(Integer.valueOf(getMessage("databaseInsertError.code")), getMessage("databaseInsertError.msg"));
    }

    // 데이터베이스 셀렉트 에러
    @ExceptionHandler(SelectDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult selectDataException(HttpServletRequest request, SelectDataException e) {
        log.error(e.toString());
        return responseService.getFailResult(Integer.valueOf(getMessage("databaseSelectError.code")), getMessage("databaseSelectError.msg"));
    }

    @ExceptionHandler(CResourceNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult resourceNotExistException(HttpServletRequest request,
        CResourceNotExistException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("resourceNotExist.code")),
            getMessage("resourceNotExist.msg"));
    }

    // validation check 오류 리턴을 위한 메소드
    // 모든 form 에 대한 오류를 리턴하지 않고, 하나만...
    // 2021.3.22 한현수 추가
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleValidationExceptions(
        MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            /*
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
             */
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put("msg", fieldName + "은(는) " + errorMsg);
        });
        return responseService.getFailResult(444, errors.get("msg"));
        //return ResponseEntity.badRequest().body(errors);
    }

    // code정보에 해당하는 메시지를 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
