package xyz.fz.consumer.controller;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.common.param.order.OrderParam;
import xyz.fz.consumer.rpc.ProviderRpc;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private ProviderRpc providerRpc;

    @RequestMapping("/create")
    public String create() {
        OrderParam orderParam = new OrderParam();
        OrderParam.OrderDetail orderDetail = new OrderParam.OrderDetail();
        orderDetail.setProductId(1);
        orderDetail.setProductCount(1);
        orderParam.setOrderDetailList(Lists.newArrayList(orderDetail));
        return providerRpc.create(orderParam);
    }
}
