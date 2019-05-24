package xyz.fz.consumer.rpc;

import org.springframework.stereotype.Component;

@Component
public class HelloFallback implements HelloRpc {
    @Override
    public String hello() {
        return "hello fallback";
    }
}
