package team.klover.server.global.redis;

public class RedisConst {
    public static final int TOKEN_TIMEOUT = 7;
    public static final String MESSAGE_LIST_PREFIX = "FCM:notifications:";  // 각 유저별 알림을 저장 할 키 prefix
    public static final String TOKEN_KEY_PREFIX = "FCM:token:"; // 각 유저의 FCM TOKEN KEY prefix
    public static final int MAX_MESSAGE_COUNT = 50 - 1;  // 최대 메시지 개수 0부터 시작하므로 -1
}