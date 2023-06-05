package br.com.mundim.rede.social.repository;

import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("SELECT p.postsId FROM Page p WHERE p.id = ?1")
    List<Long> findAllPosts(Long pageId);

    Page findPageByPageName(String username);

}
