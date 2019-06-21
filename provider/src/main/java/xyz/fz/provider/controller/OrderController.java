package xyz.fz.provider.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.common.param.OrderParam;

@RequestMapping("/order")
@RestController
public class OrderController {

    @RequestMapping("/create")
    public void create(@RequestBody OrderParam orderParam) {
        System.out.println(orderParam);
    }
}
