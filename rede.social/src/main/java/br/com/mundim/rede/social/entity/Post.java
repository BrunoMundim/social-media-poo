package br.com.mundim.rede.social.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "post_title", nullable = false)
    private String postTitle;
    @Column(name = "post_body", length = 2500, nullable = false)
    private String postBody;
    @Column(name = "likes")
    private List<Long> likesId;
    @Column(name = "dislikes")
    private List<Long> dislikesId;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Post(){}

    public Post(Long userId, String postTitle, String postBody) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.likesId = new ArrayList<>();
        this.dislikesId = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public List<Long> getLikesId() {
        return likesId;
    }

    public void setLikesId(List<Long> likesId) {
        this.likesId = likesId;
    }

    public List<Long> getDislikesId() {
        return dislikesId;
    }

    public void setDislikesId(List<Long> dislikesId) {
        this.dislikesId = dislikesId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
