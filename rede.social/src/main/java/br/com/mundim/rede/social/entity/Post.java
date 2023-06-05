package br.com.mundim.rede.social.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "page_id")
    private String pagename;
    @Column(name = "post_title", nullable = false)
    private String postTitle;
    @Column(name = "post_body", length = 2500, nullable = false)
    private String postBody;
    @Column(name = "likes")
    private List<Long> likesId;
    @Column(name = "dislikes")
    private List<Long> dislikesId;
    @Column(name = "createdAt", nullable = false)
    private String createdAt;
    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;
    @Column(name = "comments_ids")
    private List<Long> commentsIds;

    public Post(Long userId, String pagename, String postTitle, String postBody) {
        this.userId = userId;
        this.pagename = pagename;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.likesId = new ArrayList<>();
        this.dislikesId = new ArrayList<>();
        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        this.updatedAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

}
