package xyz.fz.provider.task;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.entity.CallMsg;
import xyz.fz.common.util.MsgUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("provider3")
@Component
public class CallMessageFailureTask {

    @Resource
    private CommonDao db;

    @Resource
    private MsgUtil msgUtil;

    @Scheduled(fixedRate = 30000)
    public void sendFailureCallMsg() {
        DateTime now = DateTime.now();
        String tenSecondsAgo = now.minusSeconds(10).toString("yyyy-MM-dd HH:mm:ss");
        String tenMinutesAgo = now.minusMinutes(10).toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", tenMinutesAgo);
        params.put("endTime", tenSecondsAgo);
        List<CallMsg> list = db.queryListBySql("select * from t_call_msg " +
                "where status = 0 and retryTimes < 10 " +
                "and updateTime >= :beginTime and updateTime <= :endTime ", params, CallMsg.class);
        for (CallMsg callMsg : list) {
            msgUtil.redoCallMsg(callMsg);
        }
    }
}
