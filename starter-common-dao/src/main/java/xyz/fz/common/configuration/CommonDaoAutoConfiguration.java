package xyz.fz.common.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import xyz.fz.common.dao.CommonDao;
import xyz.fz.common.dao.impl.CommonDaoImpl;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(value = {DataSource.class, EmbeddedDatabaseType.class})
public class CommonDaoAutoConfiguration {
    @Bean
    public CommonDao db() {
        return new CommonDaoImpl();
    }
}
