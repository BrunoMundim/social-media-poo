package br.com.mundim.rede.social.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "users")
@Data
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
    @Column(name = "following")
    private List<String> following; // "Tipo ID" - Tipo: PAGE or USER
    @Column(name = "followed")
    private List<String> followed; // "Tipo ID" - Tipo: PAGE or USER

    public User() {
    }

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
