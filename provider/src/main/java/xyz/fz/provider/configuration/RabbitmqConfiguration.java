package xyz.fz.provider.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    public static final String EXCHANGE_CALL_MSG = "exchange.provider";

    public static final String QUEUE_CALL_MSG = "queue.provider";

    public static final String ROUTING_CALL_MSG = "routing.provider";

    @Bean
    public TopicExchange callMsgExchange() {
        return new TopicExchange(EXCHANGE_CALL_MSG);
    }

    @Bean
    public Queue callMsgQueue() {
        return new Queue(QUEUE_CALL_MSG);
    }

    @Bean
    public Binding callMsgBinding(@Qualifier("callMsgQueue") Queue callMsgQueue, @Qualifier("callMsgExchange") TopicExchange callMsgExchange) {
        return BindingBuilder.bind(callMsgQueue).to(callMsgExchange).with(ROUTING_CALL_MSG);
    }
}
