package team.klover.server.domain.community.commPost.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.req.XYForm;
import team.klover.server.domain.community.commPost.dto.res.CombinedPostResponse;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.dto.res.DetailCommPostDto;

import java.util.List;

public interface CommPostService {
    // 사용자 위치 주변 게시글(관광지&사용자) 조회
    CombinedPostResponse findPostsWithinRadius(@Valid XYForm xyForm, Pageable pageable);

    // 본인 게시글 조회
    Page<DetailCommPostDto> findByMemberId(Long currentMemberId, Pageable pageable);

    // 해당 게시글 상세 조회
    DetailCommPostDto findById(Long commPostId);

    // 사용자가 저장한 게시글 조회
    Page<CommPostDto> getSavedCommPostByMember(Long currentMemberId, Pageable pageable);

    // 사용자 닉네임 & 게시글 내용 검색
    Page<CommPostDto> searchByKeyword(String keyword, Pageable pageable);

    // 해당 게시글 저장
    void addCollectionCommPost(Long currentMemberId, Long commPostId);

    // 해당 게시글 저장 취소
    void deleteCollectionCommPost(Long currentMemberId, Long commPostId);

    // 게시글 좋아요
    void addCommPostLike(Long currentMemberId, Long commPostId);

    // 게시글 좋아요 취소
    void deleteCommPostLike(Long currentMemberId, Long commPostId);

    // 게시글 생성
    CommPostDto addCommPost(Long currentMemberId, @Valid CommPostForm commPostForm, List<MultipartFile> imageFiles);

    // 해당 게시글 수정
    void updateCommPost(Long currentMemberId, Long commPostId, @Valid CommPostForm commPostForm, List<MultipartFile> imageFiles);

    // 해당 게시글 삭제
    void deleteCommPost(Long currentMemberId, Long commPostId);
}
