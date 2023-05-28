package br.com.mundim.rede.social.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
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
    @Column(name = "posts_id")
    private List<Long> postsId;
    @Column(name = "moderators_id", nullable = false)
    private List<Long> ModeratorsId;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return ModeratorsId;
    }

    public void setModeratorsId(List<Long> moderatorsId) {
        ModeratorsId = moderatorsId;
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
