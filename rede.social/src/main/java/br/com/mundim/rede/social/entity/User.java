package br.com.mundim.rede.social.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "ProfilePic")
    private String profilePic; // link
    @Column(name = "createdAt", nullable = false)
    private String createdAt;
    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;
    @ElementCollection
    @Column(name = "following")
    private List<String> following; // "Tipo ID" - Tipo: PAGE or USER
    @ElementCollection
    @Column(name = "followed")
    private List<String> followed; // "Tipo ID" - Tipo: PAGE or USER

    public User(String username, String password, String profilePic) {
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
        this.createdAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        this.updatedAt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        this.following = new ArrayList<>();
        this.followed = new ArrayList<>();
    }
}
