package org.livestream.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.im.interfaces.ImOnlineRpc;
import org.livestream.im.provider.service.ImOnlineService;

/**
 * @Author idea
 * @Date: Created in 09:28 2023/7/16
 * @Description
 */
@DubboService
public class ImOnlineRpcImpl implements ImOnlineRpc {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId,appId);
    }
}
