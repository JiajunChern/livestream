package org.livestream.bank.interfaces;

import org.livestream.bank.dto.AccountTradeReqDTO;
import org.livestream.bank.dto.AccountTradeRespDTO;

/**
 * @Author idea
 * @Date: Created in 10:28 2023/8/6
 * @Description
 */
public interface ICurrencyAccountRpc {

    /**
     * 增加虚拟币
     *
     * @param userId
     * @param num
     */
    void incr(long userId, int num);

    /**
     * 扣减虚拟币
     *
     * @param userId
     * @param num
     */
    void decr(long userId, int num);

    /**
     * 查询余额
     *
     * @param userId
     * @return
     */
    Integer getBalance(long userId);


    /**
     * 专门给送礼业务调用的扣减库存逻辑
     *
     * @param accountTradeReqDTO
     */
    AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO);


}
