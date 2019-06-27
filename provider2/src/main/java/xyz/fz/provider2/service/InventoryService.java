package xyz.fz.provider2.service;

import xyz.fz.common.param.order.OrderParam;

public interface InventoryService {
    void freeze(OrderParam orderParam);
}
