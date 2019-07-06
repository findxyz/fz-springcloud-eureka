package xyz.fz.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.entity.CallMsg;
import xyz.fz.common.param.Msg;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MsgUtil implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class);

    private ObjectMapper om = new ObjectMapper();

    @Resource
    private CommonDao db;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public void saveExecuteMsg(Msg msg) {
        try {
            if (msg.getMsgId() != null && msg.getMsgId() > 0) {
                String sql = "insert into t_execute_msg(id, createTime) values(:id, :createTime);";
                Map<String, Object> params = new HashMap<>();
                params.put("id", msg.getMsgId());
                params.put("createTime", new Date());
                db.executeBySql(sql, params);
            }
        } catch (Exception e) {
            boolean duplicate = false;
            Throwable throwable = e;
            while (throwable != null) {
                if (throwable.getMessage().contains("Duplicate")) {
                    duplicate = true;
                    break;
                }
                throwable = throwable.getCause();
            }
            if (duplicate) {
                rabbitTemplate.convertAndSend(msg.getExchange(), msg.getRoutingKey(), msg.getMsgId());
            }
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T saveCallMsg(Msg msg, long msgId, Object service, String method, String exchange, String routingKey) {
        try {
            Msg newMsg = (Msg) msg.clone();
            newMsg.setMsgId(msgId);
            newMsg.setExchange(exchange);
            newMsg.setRoutingKey(routingKey);
            String sql = "insert into t_call_msg(id, service, method, param, exchange, routingKey, status, retryTimes, updateTime) ";
            sql += "values (:id, :service, :method, :param, :exchange, :routingKey, :status, :retryTimes, :updateTime);";
            Map<String, Object> params = new HashMap<>();
            params.put("id", msgId);
            params.put("service", service.getClass().getGenericInterfaces()[0].getTypeName());
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
            db.executeBySql(sql, params);
            return (T) newMsg;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone not supported exception");
        }
    }

    public void notifyExecuteMsg(Msg msg) {
        try {
            if (msg.getMsgId() != null && msg.getMsgId() > 0) {
                rabbitTemplate.convertAndSend(msg.getExchange(), msg.getRoutingKey(), msg.getMsgId());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void redoCallMsg(CallMsg callMsg) {
        String sql = "update t_call_msg set retryTimes = retryTimes + 1, updateTime = :updateTime ";
        sql += "where id = :id and status = 0 and retryTimes < 10;";
        Map<String, Object> params = new HashMap<>();
        params.put("id", callMsg.getId());
        params.put("updateTime", new Date());
        try {
            Object service = applicationContext.getBean(Class.forName(callMsg.getService()));
            Method[] methods = service.getClass().getDeclaredMethods();
            Method method = null;
            Class<?> paramClazz = null;
            for (Method m : methods) {
                if (m.getName().equals(callMsg.getMethod())) {
                    Class pc = m.getParameterTypes()[0];
                    method = m;
                    paramClazz = pc;
                }
            }
            if (method != null && paramClazz != null) {
                method.invoke(service, om.readValue(callMsg.getParam(), paramClazz));
            } else {
                throw new RuntimeException("method not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        db.executeBySql(sql, params);
    }
}
