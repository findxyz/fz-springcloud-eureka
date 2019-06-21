package xyz.fz.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.consumer.rpc.ProviderRpc;

import javax.annotation.Resource;

@RestController
@RequestMapping("/consumer")
public class HelloController {

    @Resource
    private ProviderRpc providerRpc;

    @RequestMapping("/hello")
    public String hello() {
        return providerRpc.hello();
    }

    @RequestMapping("/hello/foo")
    public String helloFoo() {
        return providerRpc.helloFoo();
    }

    @RequestMapping("/hello/bar")
    public String helloBar() {
        return providerRpc.helloBar();
    }

    @RequestMapping("/hello/void")
    public String helloVoid() {
        providerRpc.helloVoid();
        return "hello void";
    }
}
