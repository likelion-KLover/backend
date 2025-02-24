package team.klover.server.domain.community.commPost.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;

public interface CommPostService {
    // 본인 게시글 조회
    Page<CommPostDto> findByTestMemberId(Pageable pageable);
}
