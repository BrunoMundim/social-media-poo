package br.com.mundim.rede.social.repository;

import br.com.mundim.rede.social.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("SELECT p FROM Page p WHERE p.id = :pageId")
    List<Page> findAllPosts(@Param("pageId") Long pageId);

    Page findPageByPageName(String username);

    List<Page> findByPageNameContaining(String keyword);

}
