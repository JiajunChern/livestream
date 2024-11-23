package org.livestream.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.user.dto.UserLoginDTO;
import org.livestream.user.dto.UserPhoneDTO;
import org.livestream.user.interfaces.IUserPhoneRPC;
import org.livestream.user.provider.service.IUserPhoneService;

import java.util.List;

/**
 * @Author idea
 * @Date: Created in 17:20 2023/6/13
 * @Description
 */
@DubboService
public class UserPhoneRPCImpl implements IUserPhoneRPC {

    @Resource
    private IUserPhoneService userPhoneService;

    @Override
    public UserLoginDTO login(String phone) {
        return userPhoneService.login(phone);
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return userPhoneService.queryByPhone(phone);
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return userPhoneService.queryByUserId(userId);
    }
}
