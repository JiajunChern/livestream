package org.livestream.im.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.livestream.im.constants.ImMsgCodeEnum;
import org.livestream.im.core.server.common.ChannelHandlerContextCache;
import org.livestream.im.core.server.common.ImMsg;
import org.livestream.im.core.server.service.IMsgAckCheckService;
import org.livestream.im.core.server.service.IRouterHandlerService;
import org.livestream.im.dto.ImMsgBody;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author idea
 * @Date: Created in 22:35 2023/7/14
 * @Description
 */
@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void onReceive(ImMsgBody imMsgBody) {
        //需要进行消息通知的userid
        if(sendMsgToClient(imMsgBody)) {
            //当im服务器推送了消息给到客户端，然后我们需要记录下ack
            msgAckCheckService.recordMsgAck(imMsgBody, 1);
            msgAckCheckService.sendDelayMsg(imMsgBody);
        }
    }

    @Override
    public boolean sendMsgToClient(ImMsgBody imMsgBody) {
        Long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null) {
            String msgId = UUID.randomUUID().toString();
            imMsgBody.setMsgId(msgId);
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }
}
