package org.livestream.msg.provider.consumer.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.livestream.im.constants.AppIdEnum;
import org.livestream.im.dto.ImMsgBody;
import org.livestream.im.router.interfaces.rpc.ImRouterRpc;
import org.livestream.living.interfaces.dto.LivingRoomReqDTO;
import org.livestream.living.interfaces.rpc.ILivingRoomRpc;
import org.livestream.im.router.interfaces.constants.ImMsgBizCodeEnum;
import org.livestream.msg.dto.MessageDTO;
import org.livestream.msg.provider.consumer.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author idea
 * @Date: Created in 22:41 2023/7/14
 * @Description
 */
@Component
public class SingleMessageHandlerImpl implements MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleMessageHandlerImpl.class);
    private final boolean TEST_MODE = false;
    @DubboReference
    private ImRouterRpc routerRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    @Override
    public void onMsgReceive(ImMsgBody imMsgBody) {
        int bizCode = imMsgBody.getBizCode();

        // TODO: 注销 ILivingRoomRpc 开启测试
        if (TEST_MODE && ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode() == bizCode) {
            JSONObject messageDTO = JSON.parseObject(imMsgBody.getData());
            //还不是直播间业务，暂时不做过多的处理
            ImMsgBody respMsgBody = new ImMsgBody();
            //这里的userId设置的是objectId，因为是发送给对方客户端
            respMsgBody.setUserId(messageDTO.getLong("objectId"));
            respMsgBody.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
            respMsgBody.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", messageDTO.getLong("userId"));
            jsonObject.put("content", messageDTO.getString("content"));
            respMsgBody.setData(jsonObject.toJSONString());
            //将消息推送给router进行转发给im服务器
            LOGGER.info("mq 消费消息 {}", respMsgBody);
            routerRpc.sendMsg(respMsgBody);
        }

        //直播间的聊天消息
        if (ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode() == bizCode) {
            // 一个人发送 n个人接收
            // 根据roomId，appId 去调用rpc方法，获取对应的直播间内的userId
            // 创建一个list的imMsgBody对象，
            MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
            Integer roomId = messageDTO.getRoomId();
            LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
            reqDTO.setRoomId(roomId);
            reqDTO.setAppId(imMsgBody.getAppId());
            //自己不用发
            List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(reqDTO).stream().filter(x -> !x.equals(imMsgBody.getUserId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIdList)) {
                return;
            }
            List<ImMsgBody> imMsgBodies = new ArrayList<>();
            userIdList.forEach(userId -> {
                ImMsgBody respMsg = new ImMsgBody();
                respMsg.setUserId(userId);
                respMsg.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
                respMsg.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode());
                respMsg.setData(JSON.toJSONString(messageDTO));
                imMsgBodies.add(respMsg);
            });
            //暂时不做过多的处理
            routerRpc.batchSendMsg(imMsgBodies);
        }
    }
}
