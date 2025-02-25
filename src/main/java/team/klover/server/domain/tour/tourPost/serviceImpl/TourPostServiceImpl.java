package team.klover.server.domain.tour.tourPost.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.review.repository.ReviewRepository;
import team.klover.server.domain.tour.tourPost.dto.res.DetailTourPostDto;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;
import team.klover.server.domain.tour.tourPost.entity.TourPostSave;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.entity.TourPostPage;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.domain.tour.tourPost.service.TourPostService;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourPostServiceImpl implements TourPostService {
    private final TourPostRepository tourPostRepository;
    private final MemberV1Repository MemberV1Repository;
    private final ReviewRepository reviewRepository;

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    @Override
    @Transactional(readOnly = true)
    public Page<TourPostDto> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable) {
        checkPageSize(pageable.getPageSize());
        Page<TourPost> tourPosts = tourPostRepository.findByLanguageAndAreaCode(language, areaCode, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 해당 관광지 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailTourPostDto findByContentId(String contentId) {
        TourPost tourPost = tourPostRepository.findByContentId(contentId);
        return convertToDetailTourPostDto(tourPost);
    }

    // 사용자가 저장한 관광지 조회
    @Override
    @Transactional(readOnly = true)
    public Page<TourPostDto> getSavedTourPostByMember(Pageable pageable) {
        checkPageSize(pageable.getPageSize());
        Long memberId = 1L;   // 임시 - 변경 필요
        Page<TourPost> tourPosts = tourPostRepository.findSavedTourPostsByMemberId(memberId, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 사용자 언어 & 관광지명/지역명 검색
    @Override
    @Transactional(readOnly = true)
    public Page<TourPostDto> searchByLanguageAndKeyword(String language, String keyword, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<TourPost> tourPosts = tourPostRepository.searchByLanguageAndKeyword(language, keyword, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 해당 관광지 저장
    @Override
    @Transactional
    public void addCollectionTourPost(String contentId){
        Member Member = MemberV1Repository.findById(1L).orElse(null); // 임시 - 변경 필요
        TourPost tourPost = tourPostRepository.findByContentId(contentId);

        boolean alreadySaved = tourPost.getSavedMembers()
                .stream()
                .anyMatch(savedMember -> savedMember.getId().equals(1L));
        if (alreadySaved) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }
        TourPostSave tourPostSave = new TourPostSave(Member, tourPost);
        tourPost.getSavedMembers().add(tourPostSave);
    }

    // 해당 관광지 저장 취소
    @Override
    @Transactional
    public void deleteCollectionTourPost(String contentId){
        TourPost tourPost = tourPostRepository.findByContentId(contentId);
        TourPostSave tourPostSave = tourPost.getSavedMembers()
                .stream()
                .filter(m -> m.getMember().getId().equals(1L))
                .findFirst()
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        tourPost.getSavedMembers().remove(tourPostSave);
    }

    // 요청 페이지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = TourPostPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // TourPost를 TourPostDto로 변환
    private TourPostDto convertToTourPostDto(TourPost tourPost) {
        Double avgRating = reviewRepository.findAverageRatingByTourPostId(tourPost.getContentId());
        return TourPostDto.builder()
                .contentId(tourPost.getContentId())
                .commonPlaceId(tourPost.getCommonPlaceId())
                .avgRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0) // 소수점 1자리 반올림
                .title(tourPost.getTitle())
                .addr1(tourPost.getAddr1())
                .firstImage(tourPost.getFirstImage())
                .mapX(tourPost.getMapX())
                .mapY(tourPost.getMapY())
                .build();
    }

    // TourPost를 DetailTourPostDto로 변환
    private DetailTourPostDto convertToDetailTourPostDto(TourPost tourPost) {
        Double avgRating = reviewRepository.findAverageRatingByTourPostId(tourPost.getContentId());
        return DetailTourPostDto.builder()
                .contentId(tourPost.getContentId())
                .commonPlaceId(tourPost.getCommonPlaceId())
                .avgRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0) // 소수점 1자리 반올림
                .title(tourPost.getTitle())
                .addr1(tourPost.getAddr1())
                .firstImage(tourPost.getFirstImage())
                .homepage(tourPost.getHomepage())
                .mapX(tourPost.getMapX())
                .mapY(tourPost.getMapY())
                .overview(tourPost.getOverview())
                .build();
    }
}
