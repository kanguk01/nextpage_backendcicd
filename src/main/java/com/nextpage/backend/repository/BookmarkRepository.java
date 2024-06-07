package com.nextpage.backend.repository;

import com.nextpage.backend.entity.User;
import com.nextpage.backend.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);

    Optional<Bookmark> findByUserIdAndStoryId(Long userId, Long storyId);
}
