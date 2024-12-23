package org.livestream.api.service;

import org.livestream.api.vo.req.PayProductReqVO;
import org.livestream.api.vo.resp.PayProductRespVO;
import org.livestream.api.vo.resp.PayProductVO;

/**
 * @Author idea
 * @Date: Created in 08:26 2023/8/17
 * @Description
 */
public interface IBankService {

    /**
     * 查询相关的产品列表信息
     *
     * @param type
     * @return
     */
    PayProductVO products(Integer type);

    /**
     * 发起支付
     *
     * @param payProductReqVO
     * @return
     */
    PayProductRespVO payProduct(PayProductReqVO payProductReqVO);
}
