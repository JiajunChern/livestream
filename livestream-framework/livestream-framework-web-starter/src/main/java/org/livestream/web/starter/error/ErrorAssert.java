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
     * @param qiyuBaseError
     */
    public static void isNotNull(Object obj, LivestreamBaseError qiyuBaseError) {
        if (obj == null) {
            throw new LivestreamErrorException(qiyuBaseError);
        }
    }

    /**
     * 判断字符串不能为空
     *
     * @param str
     * @param qiyuBaseError
     */
    public static void isNotBlank(String str, LivestreamBaseError qiyuBaseError) {
        if (str == null || str.trim().length() == 0) {
            throw new LivestreamErrorException(qiyuBaseError);
        }
    }

    /**
     * flag == true
     *
     * @param flag
     * @param qiyuBaseError
     */
    public static void isTure(boolean flag, LivestreamBaseError qiyuBaseError) {
        if (!flag) {
            throw new LivestreamErrorException(qiyuBaseError);
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
