package org.livestream.bank.provider.dao.maper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.livestream.bank.provider.dao.po.CurrencyTradePO;

/**
 * @Author idea
 * @Date: Created in 21:16 2023/8/7
 * @Description
 */
@Mapper
public interface ICurrencyTradeMapper extends BaseMapper<CurrencyTradePO> {
}
