package com.nextpage.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "\"bookmarks\"")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"", columnDefinition = "INT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"user\"")
    private User user;

    @Column(name = "\"storyId\"", nullable = false)
    private Long storyId;

    @Column(name = "\"createdAt\"", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"isDeleted\"", nullable = false)
    private boolean isDeleted;

    protected Bookmark() {
    }

    public Bookmark(User user, Long storyId) {
        this.user = user;
        this.storyId = storyId;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public void addBookmark(User user, Bookmark bookmark) {
        user.getBookmarks().add(bookmark);
        bookmark.user = user;
    }

    public void removeBookmark(User user, Bookmark bookmark) {
        user.getBookmarks().remove(bookmark);
        bookmark.user = null;
    }
}
