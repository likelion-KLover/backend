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

    @Bean
    public Queue esCommPostUpdateQueue() { return new Queue(QueueNames.ES_COMMPOST_UPDATE.name(), true);}

    @Bean
    public Queue esCommPostCountQueue() { return new Queue(QueueNames.ES_COMMPOST_COUNT.name(), true);}

    @Bean
    public Queue esMemberUpdateQueue() { return new Queue(QueueNames.ES_MEMBER_UPDATE.name(), true);}

    @Bean
    public Queue esCommPostDeleteQueue() { return new Queue(QueueNames.ES_COMMPOST_DELETE.name(), true);}

    @Bean
    public Queue esTourPostUpdateQueue() { return new Queue(QueueNames.ES_TOURPOST_UPDATE.name(), true);}
}
