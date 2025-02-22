package team.klover.server.domain.tour.tourPost.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.tour.tourPost.dto.res.DetailTourPostDto;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;

public interface TourPostService {
    // 사용자 언어 & 지역기반 관광지 데이터 조회
    Page<TourPostDto> findByLanguageAndAreaCode(String areaCode, Pageable pageable);

    // 사용자 언어 & 관광지 상세 정보 조회
    DetailTourPostDto findByContentId(String contentId);
}
