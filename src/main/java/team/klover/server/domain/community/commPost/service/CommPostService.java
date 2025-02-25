package team.klover.server.domain.community.commPost.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.req.XYForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.dto.res.DetailCommPostDto;

public interface CommPostService {
    // 사용자 위치 주변 게시글 조회
    Page<CommPostDto> findPostsWithinRadius(@Valid XYForm xyForm, Pageable pageable);

    // 본인 게시글 조회
    Page<CommPostDto> findByTestMemberId(Pageable pageable);

    // 해당 게시글 상세 조회
    DetailCommPostDto findById(Long id);

    // 사용자가 저장한 게시글 조회
    Page<CommPostDto> getSavedCommPostByTestMember(Pageable pageable);

    // 사용자 닉네임 & 게시글 내용 검색
    Page<CommPostDto> searchByNicknameAndContent(String keyword, Pageable pageable);

    // 해당 게시글 저장
    void addCollectionCommPost(Long id);

    // 해당 게시글 저장 취소
    void deleteCollectionCommPost(Long id);

    // 게시글 좋아요
    void addCommPostLike(Long id);

    // 게시글 좋아요 취소
    void deleteCommPostLike(Long id);

    // 게시글 생성
    void addCommPost(@Valid CommPostForm commPostForm);

    // 해당 게시글 수정
    void updateCommPost(Long id, @Valid CommPostForm commPostForm);

    // 해당 게시글 삭제
    void deleteCommPost(Long id);
}
