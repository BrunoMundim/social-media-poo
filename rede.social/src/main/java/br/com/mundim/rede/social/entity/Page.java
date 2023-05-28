package br.com.mundim.rede.social.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;
    @Column(name = "page_name", nullable = false)
    private String pageName;
    @Column(name = "page_description")
    private String pageDescription;
    @ElementCollection
    @Column(name = "posts_id")
    private List<Long> postsId;
    @Column(name = "moderators_id", nullable = false)
    private List<Long> moderatorsId;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    @Column(name = "followed")
    private List<String> followed; // "Tipo ID" - Tipo: PAGE or USER

    public Page(){}

    public Page(String pageName, String pageDescription, Long moderator) {
        this.pageName = pageName;
        this.pageDescription = pageDescription;
        this.postsId = new ArrayList<>();
        this.moderatorsId = new ArrayList<>();
        this.moderatorsId.add(moderator);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.followed = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    public List<Long> getPostsId() {
        return postsId;
    }

    public void setPostsId(List<Long> postsId) {
        this.postsId = postsId;
    }

    public List<Long> getModeratorsId() {
        return moderatorsId;
    }

    public void setModeratorsId(List<Long> moderatorsId) {
        this.moderatorsId = moderatorsId;
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

    public List<String> getFollowed() {
        return followed;
    }

    public void setFollowed(List<String> followed) {
        this.followed = followed;
    }
}
