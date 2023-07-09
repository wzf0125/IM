package org.quanta.im.constants;

/**
 * Description: 返回值枚举类型
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/9
 */
public enum ResultCode {
    SUCCESS(200, "成功"),
    FAIL(400, "操作失败"),
    PERMISSION_DENIED(401, "权限不足"),
    PARAM_ERROR(402, "参数错误"),
    UNAUTHORIZED(403, "请先登录"),
    SERVER_ERROR(500, "服务器内部错误");

    Integer code;
    String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
