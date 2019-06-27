package xyz.fz.provider.rpc;

import org.springframework.stereotype.Component;
import xyz.fz.common.param.order.OrderParam;

@Component
public class Provider2Fallback implements Provider2Rpc {

    @Override
    public void freeze(OrderParam orderParam) {
        System.out.println("inventory freeze fallback");
    }
}
