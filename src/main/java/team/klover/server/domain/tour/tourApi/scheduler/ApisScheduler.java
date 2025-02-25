package team.klover.server.domain.tour.tourApi.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.klover.server.domain.tour.tourApi.service.TourApiService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class ApisScheduler {
    private final TourApiService tourApiService;

    @Value("${schedule.use}")
    private boolean useSchedule;

    // 한국관광공사_국문 관광정보 서비스_GW 사용
    @Value("${apis.api.service-key}")
    private String serviceKeyForApis;

    @Scheduled(cron = "${schedule.cron_for_apis}")
    public void getApisApiData() {
        if(useSchedule) {
            log.info("Apis스케쥴러 실행");

            int pageNo = 1;
            int numOfRows = 60000;
            String mobileOS = "ETC";   // OS구분
            String mobileApp = "Klover";// 서비스명
            String arrange = "O";     // (A=제목순, C=수정일순, D=생성일순), 대표이미지가 반드시 있음(O=제목순, Q=수정일순, R=생성일순)
            int contentTypeId = 12;    // 관광지(12) 문화시설(14) 축제/공연/행사(15) 여행코스(25) 레포츠(28) 숙박(32) 쇼핑(38) 음식(39)
            List<String> areaCodeList = Arrays.asList("1", "2", "6", "39");   // 서울(1) 인천(2) 부산(6) 제주(39)
            List<String> languageList = Arrays.asList("KorService1", "EngService1", "JpnService1", "ChsService1"); // 언어 선택

            // 개요 제외 기본 관광지 데이터 선별 및 저장
            for(String language : languageList) {
                for (String areaCode : areaCodeList) {
                    try {
                        String apiUrl = String.format(
                                "https://apis.data.go.kr/B551011/%s/areaBasedList1?pageNo=%d&numOfRows=%d&MobileOS=%s" +
                                        "&MobileApp=%s&arrange=%s&areaCode=%s&serviceKey=%s&_type=json",
                                language, pageNo, numOfRows, mobileOS, mobileApp, arrange, areaCode, serviceKeyForApis
                        );

                        URL url = new URL(apiUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        reader.close();
                        String response = responseBuilder.toString();

                        if (response.trim().startsWith("<?xml")) {
                            response = response.replaceAll("<\\?xml[^>]*>", "").trim();
                        }

                        if (response.startsWith("{")) {
                            try {
                                tourApiService.saveApis(response, language);
                            } catch (Exception e) {
                                log.error("Apis 기본 데이터 저장 중 에러 발생", e);
                            }
                        } else {
                            log.error("예상치 못한 응답 형식(기본 데이터): {}", response);
                        }
                    } catch (Exception e) {
                        log.error("Apis API 기본 데이터 호출 중 에러 발생", e);
                    }
                }
            }
            try {
                tourApiService.sortApis();
            } catch (Exception e) {
                log.error("Apis 기본 데이터 선별 중 에러 발생", e);
            }
            try {
                tourApiService.getCommonPlace();
            } catch (Exception e) {
                log.error("Apis 모든 언어에 공통으로 있는 관광지 선별 중 에러 발생", e);
            }
            log.info("기본 관광지 데이터 선별 및 저장 완료");
            try {
                tourApiService.sortAsc();
            } catch (Exception e) {
                log.error("Apis 관광지 ASC 정렬 중 에러 발생", e);
            }
            log.info("기본 관광지 데이터 정렬 완료");

//            // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
//            List<Long> contentIdList = tourApiService.getAllContentIds();
//
//            // 관광지별 개요&홈페이지 데이터 추가 및 저장
//            for(Long contentId : contentIdList) {
//                for(String language : languageList) {
//                    try {
//                        String apiUrl = String.format(
//                                "https://apis.data.go.kr/B551011/%s/detailCommon1?pageNo=%d&numOfRows=%d&MobileOS=%s" +
//                                        "&MobileApp=%s&contentId=%s&serviceKey=%s&_type=json&defaultYN=Y&firstImageYN=N" +
//                                        "&areacodeYN=N&catcodeYN=N&addrinfoYN=N&mapinfoYN=N&overviewYN=Y&transGuideYN=N",
//                                language, pageNo, numOfRows, mobileOS, mobileApp, contentId, serviceKeyForApis
//                        );
//
//                        URL url = new URL(apiUrl);
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setRequestMethod("GET");
//                        connection.setRequestProperty("Content-Type", "application/json");
//                        connection.setRequestProperty("Accept", "application/json");
//
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//                        StringBuilder responseBuilder = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            responseBuilder.append(line);
//                        }
//                        reader.close();
//                        String response = responseBuilder.toString();
//
//                        if (response.trim().startsWith("<?xml")) {
//                            response = response.replaceAll("<\\?xml[^>]*>", "").trim();
//                        }
//
//                        if (response.startsWith("{")) {
//                            try {
//                                apiService.addOverview(response);
//                            } catch (Exception e) {
//                                log.error("Apis Scheduler 개요 데이터 저장 중 에러 발생", e);
//                            }
//                        } else {
//                            log.error("예상치 못한 응답 형식(개요 데이터): {}", response);
//                        }
//                    } catch (Exception e) {
//                        log.error("Apis API 개요 데이터 호출 중 에러 발생", e);
//                    }
//                }
//            }
            log.info("Apis스케쥴러 종료");
        }
    }
}
