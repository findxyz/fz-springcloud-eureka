package xyz.fz.consumer.controller;

import org.springframework.web.bind.annotation.RequestBody;
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
    public String create(@RequestBody OrderParam orderParam) {
        providerRpc.create(orderParam);
        return "order create";
    }
}
