package br.com.mundim.rede.social.repository;

import br.com.mundim.rede.social.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long id);

    List<Post> findByPostBodyContaining(String keyword);

    List<Post> findByPostTitleContaining(String keyword);
}
