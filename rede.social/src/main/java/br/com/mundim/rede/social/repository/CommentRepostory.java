package br.com.mundim.rede.social.repository;

import br.com.mundim.rede.social.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepostory extends JpaRepository<Comment, Long> {
}
