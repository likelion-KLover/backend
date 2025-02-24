package team.klover.server.domain.tour.tourApi.service;

import java.util.List;

public interface TourApiService {
    // Apis의 데이터를 Post에 저장
    void saveApis(String jsonResponse, String language);

    // 저장된 Apis 데이터에서 선별
    void sortApis();

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    List<String> getAllContentIds();

    // 관광지별 개요 데이터 추가 및 저장
    void addOverview(String jsonResponse);

    // 관광지의 X, Y 좌표가 모든 언어에 공통으로 있는 관광지 선별
    void getCommonPlace();
}
