package team.klover.server.domain.tour.api.service;

import java.util.List;

public interface ApiService {
    // Apis의 데이터를 Post에 저장
    void saveApis(String jsonResponse, String language);

    // 저장된 Apis 데이터에서 선별
    void sortApis();

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    List<String> getAllContentIds();

    // 관광지별 개요 데이터 추가 및 저장
    void addOverview(String jsonResponse);
}
