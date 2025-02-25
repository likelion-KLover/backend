package team.klover.server.domain.community.commPost.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.req.XYForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.dto.res.DetailCommPostDto;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.entity.CommPostLike;
import team.klover.server.domain.community.commPost.entity.CommPostPage;
import team.klover.server.domain.community.commPost.entity.CommPostSave;
import team.klover.server.domain.community.commPost.repository.CommPostRepository;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommPostServiceImpl implements CommPostService {
    private final CommPostRepository commPostRepository;
    private final MemberV1Repository memberV1Repository;

    // 사용자 위치 주변 게시글 조회
    public Page<CommPostDto> findPostsWithinRadius(@Valid XYForm xyForm, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<CommPost> commPosts = commPostRepository.findPostsWithinRadius(xyForm.getMapX(), xyForm.getMapY(), xyForm.getRadius(), pageable);
        return commPosts.map(this::convertToCommPostDto);
    }

    // 본인 게시글 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommPostDto> findByMemberId(Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<CommPost> commPosts = commPostRepository.findByMemberId(1L, pageable);
        return commPosts.map(this::convertToCommPostDto);
    }

    // 해당 게시글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailCommPostDto findById(Long id){
        CommPost commPost = commPostRepository.findById(id).orElse(null);
        return convertToDetailCommPostDto(commPost);
    }

    // 사용자가 저장한 게시글 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommPostDto> getSavedCommPostByMember(Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<CommPost> commPosts = commPostRepository.findSavedCommPostByMemberId(AuthUtil.getCurrentMemberId(), pageable);
        return commPosts.map(this::convertToCommPostDto);
    }

    // 사용자 닉네임 & 게시글 내용 검색
    @Override
    @Transactional(readOnly = true)
    public Page<CommPostDto> searchByNicknameAndContent(String keyword, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<CommPost> commPosts = commPostRepository.searchByKeyword(keyword, pageable);
        return commPosts.map(this::convertToCommPostDto);
    }

    // 해당 게시글 저장
    @Override
    @Transactional
    public void addCollectionCommPost(Long id){
        Member member = memberV1Repository.findById(AuthUtil.getCurrentMemberId()).orElse(null);
        CommPost commPost = commPostRepository.findById(id).orElse(null);

        boolean alreadySaved = commPost.getSavedMembers()
                .stream()
                .anyMatch(savedMember -> savedMember.getId().equals(AuthUtil.getCurrentMemberId()));
        if (alreadySaved) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }
        CommPostSave commPostSave = new CommPostSave(member, commPost);
        commPost.getSavedMembers().add(commPostSave);
    }

    // 해당 게시글 저장 취소
    @Override
    @Transactional
    public void deleteCollectionCommPost(Long id){
        CommPost commPost = commPostRepository.findById(id).orElse(null);
        CommPostSave commPostSave = commPost.getSavedMembers()
                .stream()
                .filter(m -> m.getMember().getId().equals(AuthUtil.getCurrentMemberId()))
                .findFirst()
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        commPost.getSavedMembers().remove(commPostSave);
    }

    // 게시글 좋아요
    @Override
    @Transactional
    public void addCommPostLike(Long id){
        Member member = memberV1Repository.findById(AuthUtil.getCurrentMemberId()).orElse(null);
        CommPost commPost = commPostRepository.findById(id).orElse(null);

        boolean alreadySaved = commPost.getLikedMembers()
                .stream()
                .anyMatch(savedMember -> savedMember.getId().equals(AuthUtil.getCurrentMemberId()));
        if (alreadySaved) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }
        CommPostLike commPostLike = new CommPostLike(member, commPost);
        commPost.getLikedMembers().add(commPostLike);
    }

    // 게시글 좋아요 취소
    @Override
    @Transactional
    public void deleteCommPostLike(Long id){
        CommPost commPost = commPostRepository.findById(id).orElse(null);
        CommPostLike commPostLike = commPost.getLikedMembers()
                .stream()
                .filter(m -> m.getMember().getId().equals(AuthUtil.getCurrentMemberId()))
                .findFirst()
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        commPost.getLikedMembers().remove(commPostLike);
    }

    // 게시글 생성
    @Override
    @Transactional
    public void addCommPost(@Valid CommPostForm commPostForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(AuthUtil.getCurrentMemberId()).orElse(null);
        CommPost commPost = CommPost.builder()
                .member(member)
                .content(commPostForm.getContent())
                .nickname(member.getNickname())
                .mapX(commPostForm.getMapX())
                .mapY(commPostForm.getMapY())
                .imageUrl(commPostForm.getImageUrl())
                .build();
        commPostRepository.save(commPost);
    }

    // 해당 게시글 수정
    @Override
    @Transactional
    public void updateCommPost(Long id, @Valid CommPostForm commPostForm){
        CommPost commPost = commPostRepository.findById(id)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        Member member = memberV1Repository.findById(AuthUtil.getCurrentMemberId()).orElse(null);
        if (!commPost.getMember().getId().equals(AuthUtil.getCurrentMemberId())) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        commPost.setMapX(commPostForm.getMapX());
        commPost.setMapY(commPostForm.getMapY());
        commPost.setContent(commPostForm.getContent());
        commPost.setImageUrl(commPostForm.getImageUrl());
        commPostRepository.save(commPost);
    }

    // 해당 게시글 삭제
    @Override
    @Transactional
    public void deleteCommPost(Long id){
        CommPost commPost = commPostRepository.findById(id)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        Member member = memberV1Repository.findById(AuthUtil.getCurrentMemberId()).orElse(null);
        if (!commPost.getMember().getId().equals(AuthUtil.getCurrentMemberId())) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        commPostRepository.delete(commPost);
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
                .memberId(commPost.getMember().getId())
                .nickname(commPost.getNickname())
                .mapX(commPost.getMapX())
                .mapY(commPost.getMapY())
                .imageUrl(commPost.getImageUrl())
                .createDate(commPost.getCreateDate())
                .build();
    }

    // CommPost를 DetailCommPostDto로 변환
    private DetailCommPostDto convertToDetailCommPostDto(CommPost commPost) {
        return DetailCommPostDto.builder()
                .memberId(commPost.getMember().getId())
                .nickname(commPost.getNickname())
                .likeCount(commPost.getLikedMembers().size())
                .mapX(commPost.getMapX())
                .mapY(commPost.getMapY())
                .content(commPost.getContent())
                .imageUrl(commPost.getImageUrl())
                .createDate(commPost.getCreateDate())
                .build();
    }
}
