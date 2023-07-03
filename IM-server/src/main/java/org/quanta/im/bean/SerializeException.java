package org.quanta.im.bean;

/**
 * Description: 序列化异常
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        super(message);
    }
}
