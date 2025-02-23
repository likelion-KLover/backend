package team.klover.server.domain.tour.tourPost.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.tour.tourPost.dto.res.DetailTourPostDto;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.entity.TourPostPage;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.domain.tour.tourPost.service.TourPostService;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourPostServiceImpl implements TourPostService {
    private final TourPostRepository tourPostRepository;

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    @Override
    @Transactional(readOnly = true)
    public Page<TourPostDto> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable) {
        checkPageSize(pageable.getPageSize());
        Page<TourPost> tourPosts = tourPostRepository.findByLanguageAndAreaCode(language, areaCode, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 사용자 언어 & 관광지 상세 정보 조회
    @Override
    @Transactional(readOnly = true)
    public DetailTourPostDto findByLanguageAndContentId(String language, String contentId) {
        Optional<TourPost> tourPost = tourPostRepository.findByLanguageAndContentId(language, contentId);
        return tourPost.map(this::convertToDetailTourPostDto)
                       .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
    }

    // 사용자 언어 & 관광지명/지역명 검색
    @Override
    @Transactional(readOnly = true)
    public Page<TourPostDto> searchByLanguageAndKeyword(String language, String keyword, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<TourPost> tourPosts = tourPostRepository.searchByLanguageAndKeyword(language, keyword, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 요청 페이지 수 제한
    public void checkPageSize(int pageSize) {
        int maxPageSize = TourPostPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // TourPost를 TourPostDto로 변환
    private TourPostDto convertToTourPostDto(TourPost tourPost) {
        return TourPostDto.builder()
                .contentId(tourPost.getContentId())
                .title(tourPost.getTitle())
                .addr1(tourPost.getAddr1())
                .firstImage(tourPost.getFirstImage())
                .map_x(tourPost.getMap_x())
                .map_y(tourPost.getMap_y())
                .build();
    }

    // TourPost를 DetailTourPostDto로 변환
    private DetailTourPostDto convertToDetailTourPostDto(TourPost tourPost) {
        return DetailTourPostDto.builder()
                .contentId(tourPost.getContentId())
                .title(tourPost.getTitle())
                .addr1(tourPost.getAddr1())
                .firstImage(tourPost.getFirstImage())
                .homepage(tourPost.getHomepage())
                .map_x(tourPost.getMap_x())
                .map_y(tourPost.getMap_y())
                .overview(tourPost.getOverview())
                .build();
    }
}
