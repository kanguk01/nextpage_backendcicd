package com.nextpage.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "\"users\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"", columnDefinition = "INT")
    private Long id;

    @Column(name = "\"email\"", unique = true, nullable = false)
    private String email;

    @Column(name = "\"nickname\"", unique = true, nullable = false)
    private String nickname;

    @Column(name = "\"createdAt\"", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;

    @Column(name = "\"isDeleted\"", nullable = false)
    private boolean isDeleted;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBookmark> bookmarks = new HashSet<>();

    public User() {
    }

    @Builder
    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }

    public User update(String nickname) { // 프로필 수정 시 사용
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

    public User(Long id, String email, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public void addBookmark(UserBookmark bookmark) {
        bookmarks.add(bookmark);
    }

    public void removeBookmark(UserBookmark bookmark) {
        bookmarks.remove(bookmark);
    }
}

