package org.livestream.api.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.livestream.api.error.ApiErrorEnum;
import org.livestream.api.service.IGiftService;
import org.livestream.api.vo.req.GiftReqVO;
import org.livestream.api.vo.resp.GiftConfigVO;
import org.livestream.bank.interfaces.ICurrencyAccountRpc;
import org.livestream.common.interfaces.dto.SendGiftMq;
import org.livestream.common.interfaces.topic.GiftProviderTopicNames;
import org.livestream.common.interfaces.utils.ConvertBeanUtils;
import org.livestream.gift.dto.GiftConfigDTO;
import org.livestream.gift.interfaces.IGiftConfigRpc;
import org.livestream.web.starter.context.LivestreamRequestContext;
import org.livestream.web.starter.error.ErrorAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author idea
 * @Date: Created in 15:17 2023/8/1
 * @Description
 */
@Service
public class GiftServiceImpl implements IGiftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftServiceImpl.class);

    @DubboReference
    private IGiftConfigRpc giftConfigRpc;
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;
    @Resource
    private MQProducer mqProducer;
    private Cache<Integer,GiftConfigDTO> giftConfigDTOCache = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(90, TimeUnit.SECONDS).build();

    @Override
    public List<GiftConfigVO> listGift() {
        List<GiftConfigDTO> giftConfigDTOS = giftConfigRpc.queryGiftList();
        return ConvertBeanUtils.convertList(giftConfigDTOS, GiftConfigVO.class);
    }

    @Override
    public boolean send(GiftReqVO giftReqVO) {
        int giftId = giftReqVO.getGiftId();
        //map集合，判断本地是否有对象，如果有就返回，如果没有就rpc调用，同时注入到本地map中
        GiftConfigDTO giftConfigDTO = giftConfigDTOCache.get(giftId, id -> giftConfigRpc.getByGiftId(giftId));
        ErrorAssert.isNotNull(giftConfigDTO, ApiErrorEnum.GIFT_CONFIG_ERROR);
        ErrorAssert.isTure(!giftReqVO.getReceiverId().equals(giftReqVO.getSenderUserId()), ApiErrorEnum.NOT_SEND_TO_YOURSELF);
        SendGiftMq sendGiftMq = new SendGiftMq();
        sendGiftMq.setUserId(LivestreamRequestContext.getUserId());
        sendGiftMq.setGiftId(giftId);
        sendGiftMq.setRoomId(giftReqVO.getRoomId());
        sendGiftMq.setReceiverId(giftReqVO.getReceiverId());
        sendGiftMq.setUrl(giftConfigDTO.getSvgaUrl());
        sendGiftMq.setType(giftReqVO.getType());
        sendGiftMq.setPrice(giftConfigDTO.getPrice());
        //避免重复消费
        sendGiftMq.setUuid(UUID.randomUUID().toString());
        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.SEND_GIFT);
        message.setBody(JSON.toJSONBytes(sendGiftMq));
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[gift-send] send result is {}", sendResult);
        } catch (Exception e) {
            LOGGER.info("[gift-send] send result is error:", e);
        }
        return true;
    }
}
