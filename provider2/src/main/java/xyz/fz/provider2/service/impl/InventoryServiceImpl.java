package xyz.fz.provider2.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.param.order.OrderParam;
import xyz.fz.common.util.MsgUtil;
import xyz.fz.provider2.service.InventoryService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private CommonDao db;

    @Resource
    private MsgUtil msgUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freeze(OrderParam orderParam) {

        // 保存执行消息（幂等目的）
        msgUtil.saveExecuteMsg(orderParam);

        // 执行本地业务逻辑
        for (OrderParam.OrderDetail orderDetail : orderParam.getOrderDetailList()) {
            int productId = orderDetail.getProductId();
            int productCount = orderDetail.getProductCount();
            String sql = "update t_inventory set freezeCount = freezeCount + :productCount where productId = :productId";
            Map<String, Object> params = new HashMap<>();
            params.put("productId", productId);
            params.put("productCount", productCount);
            db.executeBySql(sql, params);
        }

        // 发送执行消息回执
        try {
            msgUtil.notifyExecuteMsg(orderParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
