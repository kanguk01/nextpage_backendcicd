package com.nextpage.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

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

    protected Bookmark() {
    }

    public Bookmark(User user, Long storyId) {
        this.user = user;
        this.storyId = storyId;
    }

    public void addBookmark(User user, Bookmark bookmark) {
        user.getBookmarks().add(bookmark);
        bookmark.setUser(user);
    }

    public void removeBookmark(User user, Bookmark bookmark) {
        user.getBookmarks().remove(bookmark);
        bookmark.setUser(null);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
