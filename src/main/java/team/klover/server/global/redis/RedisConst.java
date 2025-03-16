package team.klover.server.global.redis;

public class RedisConst {
    public static final int TOKEN_TIMEOUT = 30;
    public static final String MESSAGE_LIST_PREFIX = "FCM:notifications:";  // 각 유저별 알림을 저장 할 키 prefix
    public static final String TOKEN_KEY_PREFIX = "FCM:token:"; // 각 유저의 FCM TOKEN KEY prefix
    public static final int MAX_MESSAGE_COUNT = 50 - 1;  // 최대 메시지 개수 0부터 시작하므로 -1

    public static final String ES_COMMPOST_PREFIX = "commpost:";
    public static final String ES_COMMPOST_COUNT_POSTFIX = ":count";
    public static final String ES_COMMPOST_UPDATE_POSTFIX = ":update";
    public static final String ES_COMMPOST_DELETE_POSTFIX = ":delete";
    public static final String ES_MEMBER_PREFIX = "member:";

    public static final String ES_NICKNAMEONLY_PREFIX=":nickname";
    public static final String ES_MEMBER_UPDATE_POSTFIX=":update";
    public static final String ES_MEMBER_DELETE = "memberDelete";

    public static final String ES_TOURPOST_PREFIX = "tourpost:";
    public static final String ES_TOURPOST_COUNT_PREFIX = ":count";

}