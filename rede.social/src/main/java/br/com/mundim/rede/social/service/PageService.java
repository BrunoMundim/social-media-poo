package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository repository;

    public Page savePage(Page page){
        return repository.save(page);
    }

    public Page findPageById(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<Page> findAll(){
        return repository.findAll();
    }

    public void deletePage(Long id){
        repository.deleteById(id);
    }

    public List<Long> findAllPostsFromPage(Long pageId){
        return repository.findAllPosts(pageId);
    }

}
