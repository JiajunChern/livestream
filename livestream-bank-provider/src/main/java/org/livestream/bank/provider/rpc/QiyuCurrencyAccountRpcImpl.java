package org.livestream.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.bank.dto.AccountTradeReqDTO;
import org.livestream.bank.dto.AccountTradeRespDTO;
import org.livestream.bank.interfaces.IQiyuCurrencyAccountRpc;
import org.livestream.bank.provider.service.IQiyuCurrencyAccountService;

/**
 * @Author idea
 * @Date: Created in 10:28 2023/8/6
 * @Description
 */
@DubboService
public class QiyuCurrencyAccountRpcImpl implements IQiyuCurrencyAccountRpc {

    @Resource
    private IQiyuCurrencyAccountService qiyuCurrencyAccountService;

    @Override
    public void incr(long userId, int num) {
        qiyuCurrencyAccountService.incr(userId, num);
    }

    @Override
    public void decr(long userId, int num) {
        qiyuCurrencyAccountService.decr(userId, num);
    }

    @Override
    public Integer getBalance(long userId) {
        return qiyuCurrencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        return qiyuCurrencyAccountService.consumeForSendGift(accountTradeReqDTO);
    }

}
