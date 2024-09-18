package com.atguigu.lease.common.exception;

import com.atguigu.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Result handle(LeaseException e){
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }

}
