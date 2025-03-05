package team.klover.server.global.rabbitMQ.queueNames;

import lombok.Getter;

@Getter
public enum QueueNames {
    COMMENT_NOTIFICATION,

    COMMPOST_NOTIFICATION,

    ALL_NOTIFICATION,

    ES_COMMPOST_UPDATE,
    ES_COMMPOST_DELETE,

    ES_TOURPOST_UPDATE
}
