package org.livestream.bank.provider.service;

import org.livestream.bank.provider.dao.po.PayTopicPO;

/**
 * @Author idea
 * @Date: Created in 22:08 2023/8/19
 * @Description
 */
public interface IPayTopicService {

    /**
     * 根据code查询
     *
     * @param code
     * @return
     */
    PayTopicPO getByCode(Integer code);
}
