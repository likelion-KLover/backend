package team.klover.server.global.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

import java.time.Duration;

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
