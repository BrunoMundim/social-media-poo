package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.PageDTO;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/pages")
public class PageController {

    @Autowired
    private PageService service;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Page> savePage(@RequestBody PageDTO pageDTO){
        Page page = new Page();
        page.setPageName(pageDTO.getPageName());
        page.setPageDescription(pageDTO.getPageDescription());
        List<Long> moderators = new ArrayList<>();
        moderators.add(pageDTO.getCreatorId());
        page.setModeratorsId(moderators);
        page.setPostsId(new ArrayList<>());
        page.setCreatedAt(LocalDateTime.now());
        page.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(service.savePage(page));
    }

    @PutMapping("/moderators/delete")
    public ResponseEntity<?> deleteModeratorsPage(@RequestParam Long pageId, Long moderatorId){
        Page page = service.findPageById(pageId);
        List<Long> moderatorsIds = page.getModeratorsId();
        // Verificar se usuário existe
        if(userService.findById(moderatorId) == null)
            return new ResponseEntity<String>("O usuário com id " + moderatorId + " não existe", HttpStatus.BAD_REQUEST);
        // Verificar se usuário é moderador
        if(!moderatorsIds.contains(moderatorId))
            return new ResponseEntity<String>("O usuário com id " + moderatorId + " não é moderador", HttpStatus.BAD_REQUEST);
        // Verificar se o número de moderadores é maior que o mínimo
        if(moderatorsIds.size() == 1)
            return new ResponseEntity<String>("O número de moderadores não pode ser menor que 1.", HttpStatus.BAD_REQUEST);
        // Deletar moderador
        moderatorsIds.remove(moderatorId);
        page.setModeratorsId(moderatorsIds);
        page.setUpdatedAt(LocalDateTime.now());
        return new ResponseEntity<Page>(service.savePage(page), HttpStatus.OK);
    }

    @DeleteMapping("/moderators/add")
    public ResponseEntity<?> addModeratorsPage(@RequestParam Long pageId, Long moderatorId){
        Page page = service.findPageById(pageId);
        List<Long> moderatorsIds = page.getModeratorsId();
        // Verificar se usuário existe
        if(userService.findById(moderatorId) == null)
            return new ResponseEntity<String>("O usuário com id " + moderatorId + " não existe", HttpStatus.BAD_REQUEST);
        // Verificar se usuário já não é moderador
        if(moderatorsIds.contains(moderatorId))
            return new ResponseEntity<String>("O usuário com id " + moderatorId + " já é moderador.", HttpStatus.BAD_REQUEST);
        // Adicionar moderador
        moderatorsIds.add(moderatorId);
        page.setModeratorsId(moderatorsIds);
        page.setUpdatedAt(LocalDateTime.now());
        return new ResponseEntity<Page>(service.savePage(page), HttpStatus.OK);
    }

    @PutMapping("/new-post")
    public ResponseEntity<?> addNewPost(@RequestParam Long pageId, Long postId){
        Page page = service.findPageById(pageId);
        List<Long> postsId = page.getPostsId();
        // Verifica se o post já existe
        if(postsId.contains(postId))
            return new ResponseEntity<String>("O post com id " + postId + " já existe na página.", HttpStatus.BAD_REQUEST);
        // Adiciona o post
        postsId.add(postId);
        page.setPostsId(postsId);
        page.setUpdatedAt(LocalDateTime.now());
        return new ResponseEntity<Page>(service.savePage(page), HttpStatus.OK);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<?> deletePagePost(@RequestParam Long pageId, Long postId, Long userId){
        Page page = service.findPageById(pageId);
        Post post = postService.findPostById(postId);
        // Verifica se o usuário pode apagar o post
        if(!page.getModeratorsId().contains(userId) && !Objects.equals(post.getUserId(), userId))
            return new ResponseEntity<String>("O usuário não é dono do post ou moderador da página", HttpStatus.BAD_REQUEST);
        // Verifica se o post existe na página
        else if(!page.getPostsId().contains(postId))
            return new ResponseEntity<String>("O post não existe", HttpStatus.BAD_REQUEST);
        // Apaga o post
        List<Long> posts = page.getPostsId();
        posts.remove(postId);
        page.setPostsId(posts);
        page.setUpdatedAt(LocalDateTime.now());
        return new ResponseEntity<Page>(service.savePage(page), HttpStatus.OK);
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<Page>> findAllPages(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/find")
    public ResponseEntity<Page> findPageById(@RequestParam Long pageId){
        return ResponseEntity.ok(service.findPageById(pageId));
    }

}
