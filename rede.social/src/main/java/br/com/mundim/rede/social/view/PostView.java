package br.com.mundim.rede.social.view;

import br.com.mundim.rede.social.entity.Comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostView {

    private String userPic;
    private String username;
    private String pagePic;
    private String pagename;
    private String title;
    private String body;
    private Integer likes;
    private Integer dislikes;
    private List<Comment> comments;

    public PostView(String userPic, String username, String pagePic, String pagename, String title,
                    String body, Integer likes, Integer dislikes, List<Comment> comments) {
        this.userPic = userPic;
        this.username = username;
        this.pagePic = pagePic;
        this.pagename = pagename;
        this.title = title;
        this.body = body;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
    }
}
