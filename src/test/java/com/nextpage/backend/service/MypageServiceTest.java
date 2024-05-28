package com.nextpage.backend.service;

import com.nextpage.backend.config.jwt.TokenService;
import com.nextpage.backend.dto.response.StoryListResponseDTO;
import com.nextpage.backend.entity.Story;
import com.nextpage.backend.error.exception.story.StoryNotFoundException;
import com.nextpage.backend.error.exception.user.UserNotFoundException;
import com.nextpage.backend.repository.StoryRepository;
import com.nextpage.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MypageServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private MypageService mypageService;

    @Mock
    private HttpServletRequest request;

    private Story story1;
    private Story story2;
    private Long userId;
    private String nickname;

    @BeforeEach
    void setUp() {
        userId = 1L;
        nickname = "testNickname";
        LocalDateTime now = LocalDateTime.now();
        story1 = Story.builder()
                .id(1L)
                .content("Content1")
                .imageUrl("ImageUrl1")
                .createdAt(now)
                .updatedAt(now)
                .isDeleted(false)
                .userNickname(nickname)
                .parentId(null)
                .build();
        story2 = Story.builder()
                .id(2L)
                .content("Content2")
                .imageUrl("ImageUrl2")
                .createdAt(now)
                .updatedAt(now)
                .isDeleted(false)
                .userNickname(nickname)
                .parentId(null)
                .build();
    }

    @Test
    @DisplayName("내가 작성한 스토리 조회 -> 성공")
    void getStoriesByNickname_성공() {
        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
        when(userRepository.findNicknameById(userId)).thenReturn(Optional.of(nickname));
        when(storyRepository.findStoriesByNickname(nickname)).thenReturn(Arrays.asList(story1, story2));
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        List<StoryListResponseDTO> result = mypageService.getStoriesByNickname(request);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getContent()).isEqualTo("Content2");
        assertThat(result.get(1).getContent()).isEqualTo("Content1");

        verify(tokenService, times(1)).getUserIdFromToken(request);
        verify(userRepository, times(1)).findNicknameById(userId);
        verify(storyRepository, times(1)).findStoriesByNickname(nickname);
        verify(userRepository, times(1)).existsByNickname(nickname);
    }

    @Test
    @DisplayName("내가 작성한 스토리 조회 -> 존재하지 않는 사용자")
    void getStoriesByNickname_사용자_없음() {
        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
        when(userRepository.findNicknameById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> mypageService.getStoriesByNickname(request));

        verify(tokenService, times(1)).getUserIdFromToken(request);
        verify(userRepository, times(1)).findNicknameById(userId);
        verify(storyRepository, never()).findStoriesByNickname(anyString());
        verify(userRepository, never()).existsByNickname(anyString());
    }

    @Test
    @DisplayName("내가 작성한 스토리 조회 -> 스토리 없음")
    void getStoriesByNickname_스토리_없음() {
        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
        when(userRepository.findNicknameById(userId)).thenReturn(Optional.of(nickname));
        when(storyRepository.findStoriesByNickname(nickname)).thenReturn(Collections.emptyList());
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        assertThrows(StoryNotFoundException.class, () -> mypageService.getStoriesByNickname(request));

        verify(tokenService, times(1)).getUserIdFromToken(request);
        verify(userRepository, times(1)).findNicknameById(userId);
        verify(storyRepository, times(1)).findStoriesByNickname(nickname);
        verify(userRepository, times(1)).existsByNickname(nickname);
    }
}
