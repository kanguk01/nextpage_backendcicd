//package com.nextpage.backend.service;
//
//import com.nextpage.backend.config.jwt.TokenService;
//import com.nextpage.backend.dto.response.BookmarkResponseDTO;
//import com.nextpage.backend.dto.response.StoryListResponseDTO;
//import com.nextpage.backend.entity.Story;
//import com.nextpage.backend.entity.User;
//import com.nextpage.backend.entity.Bookmark;
//import com.nextpage.backend.error.exception.bookmark.BookmarkNotFoundException;
//import com.nextpage.backend.error.exception.story.StoryNotFoundException;
//import com.nextpage.backend.error.exception.user.UserNotFoundException;
//import com.nextpage.backend.repository.BookmarkRepository;
//import com.nextpage.backend.repository.StoryRepository;
//import com.nextpage.backend.repository.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MypageServiceTest {
//
//    @Mock
//    private StoryRepository storyRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private BookmarkRepository bookmarkRepository;
//
//    @Mock
//    private TokenService tokenService;
//
//    @InjectMocks
//    private MypageService mypageService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    private Story story1;
//    private Story story2;
//    private User user;
//    private Long userId;
//    private Long storyId1;
//    private Long storyId2;
//    private String nickname;
//
//    @BeforeEach
//    void setUp() {
//        userId = 1L;
//        storyId1 = 1L;
//        storyId2 = 2L;
//        nickname = "testNickname";
//        LocalDateTime now = LocalDateTime.now();
//        user = User.builder()
//                .email("test@example.com")
//                .nickname(nickname)
//                .build();
//        story1 = Story.builder()
//                .id(storyId1)
//                .content("Content1")
//                .imageUrl("ImageUrl1")
//                .createdAt(now)
//                .updatedAt(now)
//                .isDeleted(false)
//                .userNickname(nickname)
//                .parentId(null)
//                .build();
//        story2 = Story.builder()
//                .id(storyId2)
//                .content("Content2")
//                .imageUrl("ImageUrl2")
//                .createdAt(now)
//                .updatedAt(now)
//                .isDeleted(false)
//                .userNickname(nickname)
//                .parentId(null)
//                .build();
//    }
//
//    @Test
//    @DisplayName("내가 작성한 스토리 조회 -> 성공")
//    void getStoriesByNickname_성공() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(any(HttpServletRequest.class))).thenReturn(userId);
//        when(userRepository.findNicknameById(anyLong())).thenReturn(Optional.of(nickname));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(storyRepository.findStoriesByNickname(nickname)).thenReturn(Arrays.asList(story1, story2));
//        when(userRepository.existsByNickname(nickname)).thenReturn(true);
//
//        List<StoryListResponseDTO> result = mypageService.getStoriesByNickname(request);
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getContent()).isEqualTo("Content2");
//        assertThat(result.get(1).getContent()).isEqualTo("Content1");
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findNicknameById(userId);
//        verify(storyRepository, times(1)).findStoriesByNickname(nickname);
//        verify(userRepository, times(1)).existsByNickname(nickname);
//    }
//
//    @Test
//    @DisplayName("내가 작성한 스토리 조회 -> 존재하지 않는 사용자")
//    void getStoriesByNickname_사용자_없음() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findNicknameById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> mypageService.getStoriesByNickname(request));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findNicknameById(userId);
//        verify(storyRepository, never()).findStoriesByNickname(anyString());
//        verify(userRepository, never()).existsByNickname(anyString());
//    }
//
//    @Test
//    @DisplayName("내가 작성한 스토리 조회 -> 스토리 없음")
//    void getStoriesByNickname_스토리_없음() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findNicknameById(userId)).thenReturn(Optional.of(nickname));
//        when(storyRepository.findStoriesByNickname(nickname)).thenReturn(Collections.emptyList());
//        when(userRepository.existsByNickname(nickname)).thenReturn(true);
//
//        assertThrows(StoryNotFoundException.class, () -> mypageService.getStoriesByNickname(request));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findNicknameById(userId);
//        verify(storyRepository, times(1)).findStoriesByNickname(nickname);
//        verify(userRepository, times(1)).existsByNickname(nickname);
//    }
//
//    @Test
//    @DisplayName("북마크 추가 -> 성공")
//    void addBookmark_성공() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(storyRepository.findById(storyId1)).thenReturn(Optional.of(story1));
//
//        mypageService.addBookmark(request, storyId1);
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(storyRepository, times(1)).findById(storyId1);
//        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
//    }
//
//    @Test
//    @DisplayName("북마크 추가 -> 사용자 없음")
//    void addBookmark_사용자_없음() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> mypageService.addBookmark(request, storyId1));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(storyRepository, never()).findById(anyLong());
//        verify(bookmarkRepository, never()).save(any(Bookmark.class));
//    }
//
//    @Test
//    @DisplayName("북마크 추가 -> 스토리 없음")
//    void addBookmark_스토리_없음() {
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(storyRepository.findById(storyId1)).thenReturn(Optional.empty());
//
//        assertThrows(StoryNotFoundException.class, () -> mypageService.addBookmark(request, storyId1));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(storyRepository, times(1)).findById(storyId1);
//        verify(bookmarkRepository, never()).save(any(Bookmark.class));
//    }
//
//    @Test
//    @DisplayName("북마크 조회 -> 성공")
//    void getBookmarks_성공() {
//        Bookmark bookmark1 = Bookmark.of(user, story1);
//        Bookmark bookmark2 = Bookmark.of(user, story2);
//
//        doNothing().when(tokenService).validateAccessToken(request);
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(bookmarkRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(bookmark1, bookmark2));
//        when(storyRepository.findById(storyId1)).thenReturn(Optional.of(story1));
//        when(storyRepository.findById(storyId2)).thenReturn(Optional.of(story2));
//
//        List<BookmarkResponseDTO> result = mypageService.getBookmarks(request);
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getImageUrl()).isEqualTo("imageUrl1");
//        assertThat(result.get(1).getImageUrl()).isEqualTo("imageUrl2");
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookmarkRepository, times(1)).findByUserId(user.getId());
//        verify(storyRepository, times(1)).findById(storyId1);
//        verify(storyRepository, times(1)).findById(storyId2);
//    }
//
//    @Test
//    @DisplayName("북마크 조회 -> 사용자 없음")
//    void getBookmarks_사용자_없음() {
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> mypageService.getBookmarks(request));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookmarkRepository, never()).findByUserId(anyLong());
//        verify(storyRepository, never()).findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("북마크 삭제 -> 성공")
//    void deleteBookmark_성공() {
//        Bookmark bookmark = Bookmark.of(user, story1);
//
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(bookmarkRepository.findByUserIdAndStoryId(userId, storyId1)).thenReturn(Optional.of(bookmark));
//
//        mypageService.deleteBookmark(request, storyId1);
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookmarkRepository, times(1)).findByUserIdAndStoryId(userId, storyId1);
//        verify(bookmarkRepository, times(1)).delete(bookmark);
//    }
//
//    @Test
//    @DisplayName("북마크 삭제 -> 사용자 없음")
//    void deleteBookmark_사용자_없음() {
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> mypageService.deleteBookmark(request, storyId1));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookmarkRepository, never()).findByUserIdAndStoryId(anyLong(), anyLong());
//        verify(bookmarkRepository, never()).delete(any(Bookmark.class));
//    }
//
//    @Test
//    @DisplayName("북마크 삭제 -> 북마크 없음")
//    void deleteBookmark_북마크_없음() {
//        when(tokenService.getUserIdFromToken(request)).thenReturn(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(bookmarkRepository.findByUserIdAndStoryId(userId, storyId1)).thenReturn(Optional.empty());
//
//        assertThrows(BookmarkNotFoundException.class, () -> mypageService.deleteBookmark(request, storyId1));
//
//        verify(tokenService, times(1)).getUserIdFromToken(request);
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookmarkRepository, times(1)).findByUserIdAndStoryId(userId, storyId1);
//        verify(bookmarkRepository, never()).delete(any(Bookmark.class));
//    }
//}
