package team.klover.server.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.notification.dto.FCMTokenParam;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.redis.RedisService;

import java.util.List;

@RestController
@Tag(name = "ApiV1CommentController", description = "Comment API")
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class ApiV1NotificationController {

    private final RedisService redisService;

    @Data
    private static class NotificationRequest {
        private int page = 0;
        private int limit = 10;
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "알림 전체 조회")
    public ApiResponse<?> findAllNotificationsByMemberId(@ModelAttribute NotificationRequest request, @PathVariable("memberId") Long memberId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit());

        // 전체 메시지 가져오기
        List<String> allMessages = redisService.getFCMMessages(memberId);

        // Pageable 기반 메시지 추출
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allMessages.size());
        List<String> pagedMessages = allMessages.subList(start, end);

        // PageImpl로 페이징 처리된 결과 생성
        Page<String> messagePage = new PageImpl<>(pagedMessages, pageable, allMessages.size());

        return ApiResponse.of(KloverPage.of(messagePage));
    }

    @PostMapping("/token")
    @Operation(summary = "FCM 토큰 저장 또는 업데이트")
    public ApiResponse<?> saveOrUpdateFCMToken(@RequestBody @Valid FCMTokenParam param) {
        redisService.saveOrUpdateFCMToken(param);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}