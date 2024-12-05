package org.livestream.web.starter.error;


/**
 * @Author idea
 * @Date: Created in 11:18 2023/8/2
 * @Description
 */
public class ErrorAssert {


    /**
     * 判断参数不能为空
     *
     * @param obj
     * @param baseError
     */
    public static void isNotNull(Object obj, LivestreamBaseError baseError) {
        if (obj == null) {
            throw new LivestreamErrorException(baseError);
        }
    }

    /**
     * 判断字符串不能为空
     *
     * @param str
     * @param baseError
     */
    public static void isNotBlank(String str, LivestreamBaseError baseError) {
        if (str == null || str.trim().length() == 0) {
            throw new LivestreamErrorException(baseError);
        }
    }

    /**
     * flag == true
     *
     * @param flag
     * @param baseError
     */
    public static void isTure(boolean flag, LivestreamBaseError baseError) {
        if (!flag) {
            throw new LivestreamErrorException(baseError);
        }
    }

    /**
     * flag == true
     *
     * @param flag
     * @param qiyuErrorException
     */
    public static void isTure(boolean flag, LivestreamErrorException qiyuErrorException) {
        if (!flag) {
            throw qiyuErrorException;
        }
    }
}
