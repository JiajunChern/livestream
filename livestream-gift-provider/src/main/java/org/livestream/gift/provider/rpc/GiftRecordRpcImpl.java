package org.livestream.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.gift.dto.GiftRecordDTO;
import org.livestream.gift.interfaces.IGiftRecordRpc;
import org.livestream.gift.provider.service.IGiftRecordService;

/**
 * @Author idea
 * @Date: Created in 15:10 2023/7/30
 * @Description
 */
@DubboService
public class GiftRecordRpcImpl implements IGiftRecordRpc {

    @Resource
    private IGiftRecordService giftRecordService;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        giftRecordService.insertOne(giftRecordDTO);
    }
}
