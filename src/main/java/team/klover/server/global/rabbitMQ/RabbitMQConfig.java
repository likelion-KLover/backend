package team.klover.server.global.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue commentQueue() {
        return new Queue(QueueNames.COMMENT_NOTIFICATION.name(), true);
    }

    @Bean
    public Queue commPostQueue() {
        return new Queue(QueueNames.COMMPOST_NOTIFICATION.name(), true);
    }
}
