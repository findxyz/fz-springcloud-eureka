package xyz.fz.common.configuration;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.util.MsgUtil;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(value = {DataSource.class, EmbeddedDatabaseType.class, RabbitTemplate.class, Channel.class, CommonDao.class})
public class MsgUtilAutoConfiguration {
    @Bean
    public MsgUtil msgUtil() {
        return new MsgUtil();
    }
}
