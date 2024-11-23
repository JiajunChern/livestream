package org.livestream.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.livestream.bank.dto.PayOrderDTO;
import org.livestream.bank.interfaces.IPayOrderRpc;
import org.livestream.bank.provider.dao.po.PayOrderPO;
import org.livestream.bank.provider.service.IPayOrderService;
import org.livestream.common.interfaces.utils.ConvertBeanUtils;

/**
 * @Author idea
 * @Date: Created in 21:02 2023/8/19
 * @Description
 */
@DubboService
public class PayOrderRpcImpl implements IPayOrderRpc {

    @Resource
    private IPayOrderService payOrderService;

    @Override
    public String insertOne(PayOrderDTO payOrderDTO) {
        return payOrderService.insertOne(ConvertBeanUtils.convert(payOrderDTO, PayOrderPO.class));
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        return payOrderService.updateOrderStatus(id, status);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        return payOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        return payOrderService.payNotify(payOrderDTO);
    }
}
