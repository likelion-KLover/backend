package team.klover.server.global.util;

public class Util {
    public static String truncateContent(String content, int length) {
        if (content.length() > length) {
            return content.substring(0, length) + "...";
        }
        return content;
    }

    public static Long safeMemberIdConverter(Object memberId) {
        Integer safety = (Integer) memberId;
        return Long.valueOf(safety);
    }
}
