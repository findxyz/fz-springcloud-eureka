package xyz.fz.consumer.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;

@Primary
@FeignClient(value = "SPRING-CLOUD-PROVIDER", fallback = HelloFallback.class)
public interface HelloRpc {

    @RequestMapping("/provider/hello")
    String hello();

    @RequestMapping("/provider/hello/foo")
    String helloFoo();

    @RequestMapping("/provider/hello/bar")
    String helloBar();
}
