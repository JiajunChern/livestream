package org.livestream.gift.interfaces;

import org.livestream.gift.dto.GiftRecordDTO;


/**
 * 礼物接口
 *
 * @Author idea
 * @Date: Created in 14:55 2023/7/30
 * @Description
 */
public interface IGiftRecordRpc {

    /**
     * 插入单个礼物信息
     *
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);

}
