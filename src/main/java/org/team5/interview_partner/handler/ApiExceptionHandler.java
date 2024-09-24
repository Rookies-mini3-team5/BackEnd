package org.team5.interview_partner.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.error.ErrorCodeIfs;
import org.team5.interview_partner.common.exception.ApiException;

@Slf4j
@RestController
@Order(value = Integer.MIN_VALUE)
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Api<Object>> apiResponseEntity(ApiException apiException){
        log.debug("", apiException);
        ErrorCodeIfs errorCode = apiException.getErrorCodeIfs();
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(Api.ERROR(errorCode,apiException.getErrorDescription()));
    }
}
