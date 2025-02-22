package team.klover.server.domain.tour.tourPost.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public Page<TourPostDto> findByLanguageAndAreaCode(String areaCode, Pageable pageable) {
        int maxPageSize = TourPostPage.getMaxPageSize();
        if (pageable.getPageSize() > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
        String language = "EngService1";
        Page<TourPost> tourPosts = tourPostRepository.findByLanguageAndAreaCode(language, areaCode, pageable);
        return tourPosts.map(this::convertToTourPostDto);
    }

    // 사용자 언어 & 관광지 상세 정보 조회
    public DetailTourPostDto findByContentId(String contentId) {
        Optional<TourPost> tourPost = tourPostRepository.findByContentId(contentId);
        return tourPost.map(this::convertToDetailTourPostDto)
                       .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
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
                .map_x(tourPost.getMap_x())
                .map_y(tourPost.getMap_y())
                .overview(tourPost.getOverview())
                .build();
    }
}
