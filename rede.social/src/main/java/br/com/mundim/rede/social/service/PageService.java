package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.exceptions.BadRequestException;
import br.com.mundim.rede.social.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository repository;

    public Page savePage(Page page){
        if(repository.findPageByPageName(page.getPageName()) != null)
           throw new BadRequestException("A page with that name already exists.");
        return repository.save(page);
    }

    public Page savePage(Page page, boolean updatedPagename){
        if(updatedPagename && repository.findPageByPageName(page.getPageName()) != null)
            throw new BadRequestException("A page with that name already exists.");
        else return repository.save(page);
    }

    public List<Page> findAll(){
        return repository.findAll();
    }

    public Page findPageById(Long id){
        Page page = repository.findById(id).orElse(null);
        if(page == null) throw new BadRequestException("Page with id '" + id + "' does not exists.");
        else return page;
    }

    public Page findByPagename(String pagename){
        return repository.findPageByPageName(pagename);
    }

    public void deletePage(Long id){
        Page page = repository.findById(id).orElse(null);
        if(page == null) throw new BadRequestException("Page with id '" + id + "' does not exists.");
        repository.deleteById(id);
    }

    public List<Long> findAllPostsFromPage(Long pageId) {
        Page page = repository.findById(pageId).orElse(null);
        if (page == null) {
            throw new BadRequestException("Page with id '" + pageId + "' does not exist.");
        }
        return page.getPostsId();
    }

}
