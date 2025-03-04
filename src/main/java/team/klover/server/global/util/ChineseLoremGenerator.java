package team.klover.server.global.util;

import java.util.Random;

public class ChineseLoremGenerator {
    private static final String[] WORDS = {"今天", "天气", "很好", "我们", "去", "公园", "玩", "吧", "这个", "是", "一个", "测试"};

    public static String generate(int wordCount) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            text.append(WORDS[random.nextInt(WORDS.length)]).append(" ");
        }
        return text.toString();
    }

}
