package xyz.fz.provider.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.param.order.OrderParam;
import xyz.fz.common.util.MsgUtil;
import xyz.fz.common.util.SnowFlake;
import xyz.fz.provider.configuration.RabbitmqConfiguration;
import xyz.fz.provider.entity.Order;
import xyz.fz.provider.entity.OrderDetail;
import xyz.fz.provider.rpc.Provider2Rpc;
import xyz.fz.provider.service.OrderService;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private CommonDao db;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private MsgUtil msgUtil;

    @Resource
    private Provider2Rpc provider2Rpc;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(OrderParam orderParam) {

        // 保本本次需要执行的msg到本地（幂等目的）
        msgUtil.saveExecuteMsg(orderParam);

        // 保存本次需要远程调用的msg到本地（补偿重试目的CallMsgListener）
        OrderParam orderParam2 = msgUtil.saveCallMsg(orderParam, snowFlake.generateNextId(),
                provider2Rpc, "freeze", RabbitmqConfiguration.EXCHANGE_CALL_MSG, RabbitmqConfiguration.ROUTING_CALL_MSG);

        // 执行本地业务逻辑
        Order order = new Order();
        order.setId(snowFlake.generateNextId());
        order.setAccountId(0L);
        order.setCreateTime(new Date());
        order.setStatus("create");
        db.save(order);
        for (OrderParam.OrderDetail orderDetailParam : orderParam.getOrderDetailList()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(snowFlake.generateNextId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(orderDetailParam.getProductId());
            orderDetail.setProductCount(orderDetailParam.getProductCount());
            db.save(orderDetail);
        }

        // 忽略失败情况的远程调用
        try {
            provider2Rpc.freeze(orderParam2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 执行消息回执
        try {
            msgUtil.notifyExecuteMsg(orderParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "order create success";
    }
}
