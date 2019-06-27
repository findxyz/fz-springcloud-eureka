package xyz.fz.provider.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.fz.common.util.SnowFlake;

@Configuration
public class SnowFlakeConfiguration {

    @Value("${data.center.id}")
    private int dataCenterId;

    @Value("${machine.id}")
    private int machineId;

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(dataCenterId, machineId);
    }
}
