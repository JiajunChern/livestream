package org.livestream.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.bank.dto.AccountTradeReqDTO;
import org.livestream.bank.dto.AccountTradeRespDTO;
import org.livestream.bank.interfaces.ICurrencyAccountRpc;
import org.livestream.bank.provider.service.ICurrencyAccountService;

/**
 * @Author idea
 * @Date: Created in 10:28 2023/8/6
 * @Description
 */
@DubboService
public class CurrencyAccountRpcImpl implements ICurrencyAccountRpc {

    @Resource
    private ICurrencyAccountService currencyAccountService;

    @Override
    public void incr(long userId, int num) {
        currencyAccountService.incr(userId, num);
    }

    @Override
    public void decr(long userId, int num) {
        currencyAccountService.decr(userId, num);
    }

    @Override
    public Integer getBalance(long userId) {
        return currencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        return currencyAccountService.consumeForSendGift(accountTradeReqDTO);
    }

}
