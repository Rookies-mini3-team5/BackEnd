package org.team5.interview_partner.common.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.common.error.ErrorCodeIfs;

@Data
@AllArgsConstructor
@NoArgsConstructor
//공통 응답 class
public class Api<T> {
    private Result result;

    @Valid
    private T body;
    // 정상 응답
    public static <T> Api<T> OK(T data){
        var api = new Api<T>();
        api.result = Result.OK();
        api.body = data;
        return api;
    }

    public static <T> Api<T> CREATE(){
        var api = new Api<T>();
        api.result = Result.OK();
        return api;
    }
    //에러 응답
    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs){
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }
    //에러 및 설명 응답
    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs,String description){
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs,description);
        return api;
    }
}