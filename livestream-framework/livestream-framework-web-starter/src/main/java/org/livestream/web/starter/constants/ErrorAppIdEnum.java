package org.livestream.web.starter.constants;

/**
 * @Author idea
 * @Date: Created in 15:43 2023/8/2
 * @Description
 */
public enum ErrorAppIdEnum {

    LIVESTREAM_API_ERROR(101, "livestream-api");

    ErrorAppIdEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    int code;
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
