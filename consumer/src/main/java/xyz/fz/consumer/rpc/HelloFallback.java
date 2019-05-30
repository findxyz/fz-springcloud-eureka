package xyz.fz.consumer.rpc;

import org.springframework.stereotype.Component;

@Component
public class HelloFallback implements HelloRpc {
    @Override
    public String hello() {
        return "hello fallback";
    }

    @Override
    public String helloFoo() {
        return "hello foo fallback";
    }

    @Override
    public String helloBar() {
        return "hello bar fallback";
    }
}
