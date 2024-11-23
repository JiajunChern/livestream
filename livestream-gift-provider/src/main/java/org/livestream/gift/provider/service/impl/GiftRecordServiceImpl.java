package org.livestream.gift.provider.service.impl;

import jakarta.annotation.Resource;
import org.livestream.common.interfaces.utils.ConvertBeanUtils;
import org.livestream.gift.dto.GiftRecordDTO;
import org.livestream.gift.provider.dao.mapper.GiftRecordMapper;
import org.livestream.gift.provider.dao.po.GiftRecordPO;
import org.livestream.gift.provider.service.IGiftRecordService;
import org.springframework.stereotype.Service;

/**
 * @Author idea
 * @Date: Created in 15:11 2023/7/30
 * @Description
 */
@Service
public class GiftRecordServiceImpl implements IGiftRecordService {

    @Resource
    private GiftRecordMapper giftRecordMapper;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        GiftRecordPO giftRecordPO = ConvertBeanUtils.convert(giftRecordDTO,GiftRecordPO.class);
        giftRecordMapper.insert(giftRecordPO);
    }
}
