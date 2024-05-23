package com.nextpage.backend.service;

import com.nextpage.backend.config.jwt.TokenService;
import com.nextpage.backend.dto.response.RootResponseDTO;
import com.nextpage.backend.dto.response.StoryDetailsResponseDTO;
import com.nextpage.backend.entity.Story;
import com.nextpage.backend.error.exception.story.StoryNotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoryServiceTest {
    @InjectMocks
    private StoryService storyService;
    @Mock
    private StoryRepository storyRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    private Story story;
    private Story parentStory;

    @BeforeEach
    void setUp() {
        parentStory = Story.builder()
                .id(2L)
                .content("Parent content")
                .imageUrl("ParentImageUrl")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .userNickname("parentNickname")
                .parentId(null)
                .build();

        story = Story.builder()
                .id(1L)
                .content("Content")
                .imageUrl("ImageUrl")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .userNickname("testNickname")
                .parentId(parentStory)
                .build();
    }

    @DisplayName("루트 스토리 조회 -> 성공")
    @Test
    void getRootStories_성공(){
        when(storyRepository.findRootStories()).thenReturn(Arrays.asList(story));

        List<RootResponseDTO> rootStories = storyService.getRootStories();

        assertThat(rootStories).isNotEmpty();
        assertThat(rootStories.get(0).getId()).isEqualTo(story.getId());
        verify(storyRepository, times(1)).findRootStories();
    }

    @DisplayName("루트 스토리 조회 -> 스토리가 존재하지 않음")
    @Test
    void getRootStories_스토리가_존재하지_않음() {
        when(storyRepository.findRootStories()).thenReturn(Collections.emptyList());

        assertThrows(StoryNotFoundException.class, () -> storyService.getRootStories());
    }

    @DisplayName("스토리 상세 조회 -> 성공")
    @Test
    void getStoryDetails_성공() {
        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
        when(storyRepository.findChildByParentId(1L)).thenReturn(Arrays.asList(story));

        StoryDetailsResponseDTO storyDetails = storyService.getStoryDetails(1L);

        assertThat(storyDetails).isNotNull();
        assertThat(storyDetails.getId()).isEqualTo(story.getId());
        verify(storyRepository, times(1)).findById(1L);
    }

    @DisplayName("스토리 상세 조회 -> 존재하지 않는 스토리")
    @Test
    void getStoryDetails_존재하지_않는_스토리() {
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StoryNotFoundException.class, () -> storyService.getStoryDetails(1L));
    }
}