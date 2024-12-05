package org.livestream.bank.provider.service.impl;

import jakarta.annotation.Resource;
import org.livestream.bank.provider.dao.maper.ICurrencyTradeMapper;
import org.livestream.bank.provider.dao.po.CurrencyTradePO;
import org.livestream.bank.provider.service.ICurrencyTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author idea
 * @Date: Created in 21:16 2023/8/7
 * @Description
 */
@Service
public class CurrencyTradeServiceImpl implements ICurrencyTradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyTradeServiceImpl.class);

    @Resource
    private ICurrencyTradeMapper currencyTradeMapper;

    @Override
    public boolean insertOne(long userId, int num, int type) {
        try {
            CurrencyTradePO tradePO = new CurrencyTradePO();
            tradePO.setUserId(userId);
            tradePO.setNum(num);
            tradePO.setType(type);
            currencyTradeMapper.insert(tradePO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[CurrencyTradeServiceImpl] insert error is:", e);
        }
        return false;
    }
}
