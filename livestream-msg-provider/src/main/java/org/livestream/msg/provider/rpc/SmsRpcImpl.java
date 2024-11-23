package org.livestream.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.msg.dto.MsgCheckDTO;
import org.livestream.msg.enums.MsgSendResultEnum;
import org.livestream.msg.interfaces.ISmsRpc;
import org.livestream.msg.provider.service.ISmsService;

/**
 * @Author idea
 * @Date: Created in 17:20 2023/6/11
 * @Description
 */
@DubboService
public class SmsRpcImpl implements ISmsRpc {

    @Resource
    private ISmsService smsService;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone,code);
    }

}