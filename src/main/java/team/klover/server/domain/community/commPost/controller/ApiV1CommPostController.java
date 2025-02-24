package team.klover.server.domain.community.commPost.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.tour.review.entity.ReviewPage;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;

@RestController
@RequestMapping("/api/v1/comm-post")
@RequiredArgsConstructor
public class ApiV1CommPostController {
    private final CommPostService commPostService;

    // 사용자 위치 주변 게시글 조회

    // 본인 게시글 조회
    @GetMapping("/me")
    public ApiResponse<CommPostDto> getMyCommPost(@ModelAttribute ReviewPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.findByTestMemberId(pageable)));
    }

    // 저장한 게시글 조회

    // 게시글 저장

    // 게시글 생성
    @PostMapping
    public ApiResponse<String> addCommPost(@RequestBody @Value CommPostForm commPostForm) {
        commPostService.addCommPost(commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 수정

    // 해당 게시글 삭제

}
