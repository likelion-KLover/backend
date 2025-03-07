package team.klover.server.global.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

@Configuration
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(QueueNames.COMMENT_NOTIFICATION.name(), true);
    }

    @Bean
    public Queue commPostQueue() {
        return new Queue(QueueNames.COMMPOST_NOTIFICATION.name(), true);
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3) // 최대 3번 재시도
                .backOffOptions(1000, 2.0, 5000) // 초기 1초, 2배씩 증가, 최대 5초
                .recoverer(new RejectAndDontRequeueRecoverer()) // 재시도 후 실패 시 버림 (재큐 안함)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAdviceChain(retryInterceptor()); // 재시도 정책 설정
        return factory;
    }
}
