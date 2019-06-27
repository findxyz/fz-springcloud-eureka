package xyz.fz.consumer.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.fz.common.param.order.OrderParam;

@Primary
@FeignClient(value = "SPRING-CLOUD-PROVIDER", fallback = ProviderFallback.class)
public interface ProviderRpc {

    @RequestMapping("/provider/hello")
    String hello();

    @RequestMapping("/provider/hello/foo")
    String helloFoo();

    @RequestMapping("/provider/hello/bar")
    String helloBar();

    @RequestMapping("/provider/hello/void")
    void helloVoid();

    @RequestMapping(value = "/order/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void create(@RequestBody OrderParam orderParam);
}
