package xyz.fz.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class HelloController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String hello() {
        return "hello from: " + port;
    }
}
