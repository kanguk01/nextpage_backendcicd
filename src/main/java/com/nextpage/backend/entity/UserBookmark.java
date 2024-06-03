package com.nextpage.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_bookmarks")
public class UserBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "story_id", nullable = false)
    private Long storyId;

    protected UserBookmark() {
    }

    public UserBookmark(User user, Long storyId) {
        this.user = user;
        this.storyId = storyId;
    }
}
