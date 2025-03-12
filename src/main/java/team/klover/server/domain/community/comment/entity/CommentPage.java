package team.klover.server.domain.community.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPage {
    // 기본 page, size
    private int page = 0;
    private int size = 10;
    @Getter
    private static final int maxPageSize = 10;
}
