package team.klover.server.global.elasticsearch.commpost.springevent.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.global.elasticsearch.commpost.springevent.event.*;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostCountMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostDeletionMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostModificationMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.NicknameModificationMessage;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;
import team.klover.server.global.redis.RedisService;

@Component
@RequiredArgsConstructor
public class ESCommPostEventListener {
    private final RedisService redisService;
    @EventListener
    public void handleCommPostDeletion(CommPostDeleteEvent event){
        redisService.saveCommPostDeletionMessage(new CommPostDeletionMessage(event.getCommPost()));
    }

    @EventListener
    public void handleCommPostModification(CommPostUpdateEvent event){
        redisService.saveCommPostModificationMessage(new CommPostModificationMessage(event.getCommPost()));
    }

    @EventListener
    public void handleCommPostCount(CommPostCountEvent event){
        redisService.saveCommPostCountMessage(new CommPostCountMessage(event.getCommPost()));
    }


    @EventListener
    public void handleNicknameModification(NicknameUpdateEvent event){
        redisService.saveNicknameModificationMessage(new NicknameModificationMessage(event.getMember()));
    }


}
