package org.team5.interview_partner.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.error.ErrorCode;

@Slf4j
@RestController
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception(
            Exception exception
    ){
        log.info("",exception);
        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }
}