package xyz.fz.provider2.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.common.param.order.OrderParam;
import xyz.fz.provider2.service.InventoryService;

import javax.annotation.Resource;

@RequestMapping("/inventory")
@RestController
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @RequestMapping("/freeze")
    public void freeze(@RequestBody OrderParam orderParam) {
        inventoryService.freeze(orderParam);
    }
}
