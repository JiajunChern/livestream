package org.livestream.api.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.livestream.api.service.ImService;
import org.livestream.api.vo.resp.ImConfigVO;
import org.livestream.im.constants.AppIdEnum;
import org.livestream.im.interfaces.ImTokenRpc;
import org.livestream.web.starter.context.LivestreamRequestContext;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author idea
 * @Date: Created in 10:49 2023/7/26
 * @Description
 */
@Service
public class ImServiceImpl implements ImService {

    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public ImConfigVO getImConfig() {
        ImConfigVO imConfigVO = new ImConfigVO();
        imConfigVO.setToken(imTokenRpc.createImLoginToken(LivestreamRequestContext.getUserId(), AppIdEnum.QIYU_LIVE_BIZ.getCode()));
        buildImServerAddress(imConfigVO);
        return imConfigVO;
    }

    private void buildImServerAddress(ImConfigVO imConfigVO) {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("qiyu-live-im-core-server");
        Collections.shuffle(serviceInstanceList);
        ServiceInstance aimInstance = serviceInstanceList.get(0);
        imConfigVO.setWsImServerAddress(aimInstance.getHost() + ":8086");
        imConfigVO.setTcpImServerAddress(aimInstance.getHost() + ":8085");
    }
}
