package xyz.fz.provider.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.fz.common.param.order.OrderParam;

@Primary
@FeignClient(value = "SPRING-CLOUD-PROVIDER2", fallback = Provider2Fallback.class)
public interface Provider2Rpc {

    @RequestMapping(value = "/inventory/freeze", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void freeze(@RequestBody OrderParam orderParam);
}
