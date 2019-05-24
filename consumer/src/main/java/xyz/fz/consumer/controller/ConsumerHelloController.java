package xyz.fz.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.consumer.rpc.HelloRpc;

import javax.annotation.Resource;

@RestController
@RequestMapping("/consumer")
public class ConsumerHelloController {

    @Resource
    private HelloRpc helloRpc;

    @RequestMapping("/hello")
    public String hello() {
        return helloRpc.hello();
    }
}
