package xyz.fz.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import xyz.fz.common.param.Msg;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MsgUtil implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class);

    private ObjectMapper om = new ObjectMapper();

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() {
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public boolean saveExecuteMsg(Msg msg) {
        try {
            if (msg.getMsgId() > 0) {
                String sql = "insert into t_execute_msg(id, exchange, routingKey, createTime) values(:id, :exchange, :routingKey, :createTime);";
                Map<String, Object> params = new HashMap<>();
                params.put("id", msg.getMsgId());
                params.put("exchange", msg.getExchange());
                params.put("routingKey", msg.getRoutingKey());
                params.put("createTime", new Date());
                jdbcTemplate.update(sql, params);
            }
            return true;
        } catch (DuplicateKeyException e) {
            try {
                rabbitTemplate.convertAndSend(msg.getExchange(), msg.getRoutingKey(), msg.getMsgId());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T saveCallMsg(Msg msg, long msgId, String service, String method, String exchange, String routingKey) {
        try {
            Msg newMsg = (Msg) msg.clone();
            newMsg.setMsgId(msgId);
            newMsg.setExchange(exchange);
            newMsg.setRoutingKey(routingKey);
            String sql = "insert into t_call_msg(id, service, method, param, exchange, routingKey, status, retryTimes, updateTime) ";
            sql += "values (:id, :service, :method, :param, :exchange, :routingKey, :status, :retryTimes, :updateTime);";
            Map<String, Object> params = new HashMap<>();
            params.put("id", msgId);
            params.put("service", service);
            params.put("method", method);
            try {
                params.put("param", om.writeValueAsString(newMsg));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("json processing exception");
            }
            params.put("exchange", exchange);
            params.put("routingKey", routingKey);
            params.put("status", 0);
            params.put("retryTimes", 0);
            params.put("updateTime", new Date());
            jdbcTemplate.update(sql, params);
            return (T) newMsg;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone not supported exception");
        }
    }

    public void notifyExecuteMsg(Msg msg) {
        try {
            rabbitTemplate.convertAndSend(msg.getExchange(), msg.getRoutingKey(), msg.getMsgId());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
