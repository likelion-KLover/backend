package team.klover.server.domain.community.commPost.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;

public interface CommPostService {
    // 본인 게시글 조회
    Page<CommPostDto> findByTestMemberId(Pageable pageable);

    // 사용자가 저장한 게시글 조회
    Page<CommPostDto> getSavedCommPostByTestMember(Pageable pageable);

    // 해당 게시글 저장
    void addCollectionCommPost(Long id);

    // 해당 게시글 저장 취소
    void deleteCollectionCommPost(Long id);

    // 게시글 좋아요
    void addlikeCommPost(Long id);

    // 게시글 좋아요 취소
    void deletelikeCommPost(Long id);

    // 게시글 생성
    void addCommPost(@Valid CommPostForm commPostForm);

    // 해당 게시글 수정
    void updateCommPost(Long id, @Valid CommPostForm commPostForm);

    // 해당 게시글 삭제
    void deleteCommPost(Long id);
}
