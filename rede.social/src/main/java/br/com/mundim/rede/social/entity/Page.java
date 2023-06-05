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
@Table(name = "pages")
@Data
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;
    @Column(name = "page_pic")
    private String pagePic; // link
    @Column(name = "page_name", nullable = false, unique = true)
    private String pageName;
    @Column(name = "page_description")
    private String pageDescription;
    @ElementCollection
    @Column(name = "posts_id")
    private List<Long> postsId;
    @Column(name = "moderators_id", nullable = false)
    private List<Long> moderatorsId;
    @Column(name = "createdAt", nullable = false)
    private String createdAt;
    @Column(name = "updatedAt")
    private String updatedAt;
    @Column(name = "followed")
    private List<String> followed; // "Tipo ID" - Tipo: PAGE or USER

    public Page(String pageName, String pageDescription, Long moderator) {
        this.pageName = pageName;
        this.pageDescription = pageDescription;
        this.postsId = new ArrayList<>();
        this.moderatorsId = new ArrayList<>();
        this.moderatorsId.add(moderator);
        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        this.updatedAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        this.followed = new ArrayList<>();
    }
}
