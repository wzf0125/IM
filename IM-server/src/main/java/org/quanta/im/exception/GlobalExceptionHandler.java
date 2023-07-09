package org.quanta.im.exception;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.quanta.im.bean.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.util.List;

/**
 * Description:
 * Author: wzf
 * Date: 2023/07/09
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${project.isDebug}")
    private boolean isDebug;

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    Response<Object> errorResult(Exception e){
        log.error(e.getMessage());
        // 非debug模式的话只返回服务器错误提示 不返回具体错误内容
        if (!isDebug) {
            return Response.serverError();
        }
        // 本地调试打印错误信息
        e.printStackTrace();
        return Response.serverError(String.format("操作失败，请重试[%s]", e.getMessage()));
    }

    // 数据校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    Response<Object> validationErrorResult(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        if (result.getFieldErrorCount() > 0) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
        }
        return Response.paramError(String.format("参数校验错误[%s]", sb));
    }

    @ExceptionHandler(BindException.class)
    public @ResponseBody
    Response<Object> handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        StringBuffer sb = new StringBuffer();
        if (result.getFieldErrorCount() > 0) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
        }
        return Response.paramError(String.format("参数校验错误[%s]", sb));
    }

    // 数据校验异常
    @ExceptionHandler(ValidationException.class)
    public @ResponseBody
    Response<Object> validationErrorResult(ValidationException e) {
        return Response.paramError(String.format("参数校验错误[%s]", e.getMessage()));
    }

    // 自定义参数校验异常处理类
    @ExceptionHandler(ParamException.class)
    public @ResponseBody
    Response<Object> apiErrorResult(ParamException e) {
        return Response.paramError(e.getMessage());
    }

    // 自定义异常处理类
    @ExceptionHandler(ApiException.class)
    public @ResponseBody
    Response<Object> apiErrorResult(ApiException e) {
        return Response.fail(e.getMessage());
    }

    // 权限异常校验类
    @ExceptionHandler(PermissionException.class)
    public @ResponseBody
    Response<Object> permissionError(PermissionException e) {
        return Response.permissionDenied(e.getMessage());
    }
}
