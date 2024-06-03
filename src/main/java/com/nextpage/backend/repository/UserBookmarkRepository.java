package com.nextpage.backend.repository;

import com.nextpage.backend.entity.User;
import com.nextpage.backend.entity.UserBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookmarkRepository extends JpaRepository<UserBookmark, Long> {
    List<UserBookmark> findByUser(User user);

    Optional<UserBookmark> findByUserIdAndStoryId(Long userId, Long storyId);
}
