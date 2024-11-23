package org.livestream.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.bank.dto.PayProductDTO;
import org.livestream.bank.interfaces.IPayProductRpc;
import org.livestream.bank.provider.service.IPayProductService;

import java.util.List;

/**
 * @Author idea
 * @Date: Created in 08:07 2023/8/17
 * @Description
 */
@DubboService
public class PayProductRpcImpl implements IPayProductRpc {

    @Resource
    private IPayProductService payProductService;

    @Override
    public List<PayProductDTO> products(Integer type) {
        return payProductService.products(type);
    }

    @Override
    public PayProductDTO getByProductId(Integer productId) {
        return payProductService.getByProductId(productId);
    }
}
