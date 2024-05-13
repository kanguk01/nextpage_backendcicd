package com.nextpage.backend.controller;

import com.nextpage.backend.config.jwt.TokenService;
import com.nextpage.backend.dto.response.StoryListResponseDTO;
import com.nextpage.backend.error.exception.user.UserNotFoundException;
import com.nextpage.backend.repository.UserRepository;
import com.nextpage.backend.result.ResultResponse;
import com.nextpage.backend.service.MypageService;
import com.nextpage.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.nextpage.backend.result.ResultCode.MYPAGE_MYSTORY_LIST_SUCCESS;

@Tag(name = "Mypage", description = "Mypage 관리")
@RestController
@RequestMapping("/api/v2/mypage") // 공통 api
public class MypageController {

    private final MypageService mypageService;
    private final TokenService tokenService;
    private final UserRepository userRepository;


    public MypageController(MypageService mypageService, UserService userService, TokenService tokenService, UserRepository userRepository) {
        this.mypageService = mypageService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "내가 쓴 스토리 조회", description = "특정 닉네임의 스토리를 조회합니다.")
    @GetMapping("/mystories") // 특정 분기 조회
    public ResponseEntity<ResultResponse> getStoriesByNickname(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        String nickname = userRepository.findNicknameById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<StoryListResponseDTO> storiesByNickname = mypageService.getStoriesByNickname(nickname);
        return ResponseEntity.ok(ResultResponse.of(MYPAGE_MYSTORY_LIST_SUCCESS, storiesByNickname));
    }
}
