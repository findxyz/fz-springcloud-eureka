package xyz.fz.provider.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.common.param.order.OrderParam;
import xyz.fz.provider.service.OrderService;

import javax.annotation.Resource;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/create")
    public String create(@RequestBody OrderParam orderParam) {
        return orderService.create(orderParam);
    }
}
