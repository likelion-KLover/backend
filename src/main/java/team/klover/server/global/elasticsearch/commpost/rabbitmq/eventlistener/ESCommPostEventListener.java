package team.klover.server.global.elasticsearch.commpost.rabbitmq.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.global.elasticsearch.commpost.rabbitmq.event.*;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;

@Component
@RequiredArgsConstructor
public class ESCommPostEventListener {
    private final RabbitMQProducer producer;

    @EventListener
    public void handleCommPostDeletion(CommPostDeleteEvent event){
        producer.notifyCommPostDeletion(event);
    }

    @EventListener
    public void handleCommPostModification(CommPostUpdateEvent event){
        producer.notifyCommPostModification(event);
    }

    @EventListener
    public void handleCommentCount(CommPostCountEvent event){
        producer.notifyCommPostCount(event);
    }


    @EventListener
    public void handleNicknameModification(NicknameUpdateEvent event){
        producer.notifyNicknameModification(event);
    }


}
