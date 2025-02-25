package team.klover.server.domain.tour.tourPost.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.tour.tourPost.dto.res.DetailTourPostDto;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;

public interface TourPostService {
    // 사용자 언어 & 지역기반 관광지 데이터 조회
    Page<TourPostDto> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable);

    // 해당 관광지 상세 조회
    DetailTourPostDto findByContentId(String contentId);

    // 사용자가 저장한 관광지 조회
    Page<TourPostDto> getSavedTourPostByTestMember(Pageable pageable);

    // 사용자 언어 & 관광지명/지역명 검색
    Page<TourPostDto> searchByLanguageAndKeyword(String language, String keyword, Pageable pageable);

    // 해당 관광지 저장
    void addCollectionTourPost(String contentId);

    // 해당 관광지 저장 취소
    void deleteCollectionTourPost(String contentId);
}
