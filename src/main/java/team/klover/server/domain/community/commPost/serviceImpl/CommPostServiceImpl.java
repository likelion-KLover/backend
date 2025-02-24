package team.klover.server.domain.community.commPost.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.entity.CommPostPage;
import team.klover.server.domain.community.commPost.repository.CommPostRepository;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommPostServiceImpl implements CommPostService {
    private final CommPostRepository commPostRepository;

    // 본인 게시글 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommPostDto> findByTestMemberId(Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<CommPost> commPosts = commPostRepository.findByTestMemberId(1L, pageable);
        return commPosts.map(this::convertToCommPostDto);
    }

    // 요청 페이지 수 제한
    public void checkPageSize(int pageSize) {
        int maxPageSize = CommPostPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // CommPost를 CommPostDto로 변환
    private CommPostDto convertToCommPostDto(CommPost commPost) {
        return CommPostDto.builder()
                .testMemberId(commPost.getTestMember().getId())
                .testMemberNickname(commPost.getTestMember().getNickname())
                .mapX(commPost.getMapX())
                .mapY(commPost.getMapY())
                .imageUrl(commPost.getImageUrl())
                .build();
    }
}
