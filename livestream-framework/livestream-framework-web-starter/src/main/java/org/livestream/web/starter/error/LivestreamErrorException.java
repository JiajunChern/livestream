package org.livestream.web.starter.error;

/**
 * @Author idea
 * @Date: Created in 11:15 2023/8/2
 * @Description
 */
public class LivestreamErrorException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public LivestreamErrorException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public LivestreamErrorException(LivestreamBaseError qiyuBaseError) {
        this.errorCode = qiyuBaseError.getErrorCode();
        this.errorMsg = qiyuBaseError.getErrorMsg();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
