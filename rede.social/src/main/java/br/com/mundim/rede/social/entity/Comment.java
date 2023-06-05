package br.com.mundim.rede.social.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "comment")
@Data
public class Comment {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "comment_body")
    private String commentBody;

    public Comment(Long userId, String commentBody){
        this.userId = userId;
        this.commentBody = commentBody;
    }

}
