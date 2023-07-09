package org.quanta.im.exception;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/17
 */
public class PermissionException extends RuntimeException {
    public PermissionException() {
        super("权限不足");
    }

    public PermissionException(String message) {
        super(message);
    }
}
