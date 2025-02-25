package team.klover.server.domain.community.comment.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class CommentPage {
    // 기본 page, size
    private int page = 0;
    private int size = 10;
    @Getter
    private static final int maxPageSize = 10;
}
