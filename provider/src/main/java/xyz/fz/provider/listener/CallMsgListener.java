package xyz.fz.provider.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.provider.configuration.RabbitmqConfiguration;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class CallMsgListener {

    @Resource
    private CommonDao db;

    private final HashSet<Long> msgIds = new HashSet<>();

    @RabbitListener(queues = {RabbitmqConfiguration.QUEUE_CALL_MSG})
    @Transactional(rollbackFor = Exception.class)
    public void receiveCallMsg(Long msgId) {
        synchronized (msgIds) {
            msgIds.add(msgId);
        }
    }

    @Scheduled(fixedRate = 1000)
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateCallMsg() {
        Map<String, Object> params = new HashMap<>();
        synchronized (msgIds) {
            if (msgIds.size() > 0) {
                params.put("ids", msgIds.clone());
                msgIds.clear();
            } else {
                return;
            }
        }
        params.put("updateTime", new Date());
        db.executeBySql("update t_call_msg set status = 1, updateTime = :updateTime where id in (:ids)", params);
    }
}
