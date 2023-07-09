package org.quanta.im.exception;

/**
 * Description: 自定义异常
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/10
 */
public class ParamException extends RuntimeException {
    public ParamException() {
    }

    public ParamException(String message) {
        super("参数异常:" + message);
    }
}
