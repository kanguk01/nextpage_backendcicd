package com.nextpage.backend.service;

import com.nextpage.backend.config.jwt.TokenService;
import com.nextpage.backend.dto.response.StoryListResponseDTO;
import com.nextpage.backend.entity.Story;
import com.nextpage.backend.error.exception.story.StoryNotFoundException;
import com.nextpage.backend.error.exception.user.UserNotFoundException;
import com.nextpage.backend.repository.StoryRepository;
import com.nextpage.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MypageService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public MypageService(StoryRepository storyRepository, UserRepository userRepository, TokenService tokenService) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public List<StoryListResponseDTO> getStoriesByNickname(HttpServletRequest request) { //내가 작성한 스토리 조회
        Long userId = tokenService.getUserIdFromToken(request);
        String nickname = userRepository.findNicknameById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<Story> result= storyRepository.findStoriesByNickname(nickname);
        if(!userRepository.existsByNickname(nickname)) {
            throw new UserNotFoundException();
        }
        List<StoryListResponseDTO> stories = new ArrayList<>(); //원하는 부분만 가져오기위해 DTO 설정
        for (Story story : result) {
            StoryListResponseDTO storyListResponseDTO = new StoryListResponseDTO(
                    story.getId(),
                    story.getContent(),
                    story.getImageUrl(),
                    story.getUserNickname()
                    ); //각 자식 스토리의 새로운 DTO객체 생성
            stories.add(storyListResponseDTO); //모든 필요한 부분을 채운 객체를 추가한다.
        }
        Collections.reverse(stories);
        if (stories.isEmpty()) {
            throw new StoryNotFoundException();
        }
        return stories;
    }
}
