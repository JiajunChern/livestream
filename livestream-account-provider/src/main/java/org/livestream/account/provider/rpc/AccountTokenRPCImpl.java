package org.livestream.account.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.account.interfaces.IAccountTokenRPC;
import org.livestream.account.provider.service.IAccountTokenService;

/**
 * @Author idea
 * @Date: Created in 08:39 2023/6/21
 * @Description
 */
@DubboService
public class AccountTokenRPCImpl implements IAccountTokenRPC {

    @Resource
    private IAccountTokenService accountTokenService;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return accountTokenService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        return accountTokenService.getUserIdByToken(tokenKey);
    }
}
