package xyz.fz.consumer.rpc;

import org.springframework.stereotype.Component;
import xyz.fz.common.param.order.OrderParam;

@Component
public class ProviderFallback implements ProviderRpc {
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

    @Override
    public void helloVoid() {
        System.out.println("hello void fallback");
    }

    @Override
    public void create(OrderParam orderParam) {
        System.out.println("order create fallback");
    }
}
