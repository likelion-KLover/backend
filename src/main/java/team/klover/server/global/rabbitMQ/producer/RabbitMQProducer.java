package team.klover.server.global.rabbitMQ.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.event.*;
import team.klover.server.global.elasticsearch.commpost.springevent.message.*;
import team.klover.server.global.elasticsearch.tourpost.springevent.event.TourPostCountEvent;
import team.klover.server.global.elasticsearch.tourpost.springevent.message.TourPostCountMessage;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendNotification(String queueName, NotificationMessage message) {
        rabbitTemplate.convertAndSend(queueName, objectMapper.writeValueAsString(message));
    }
}
