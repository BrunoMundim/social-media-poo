package br.com.mundim.rede.social.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
