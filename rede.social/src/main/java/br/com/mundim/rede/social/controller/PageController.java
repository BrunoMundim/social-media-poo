package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.PageDTO;
import br.com.mundim.rede.social.dto.PostDTO;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.exceptions.BadRequestException;
import br.com.mundim.rede.social.exceptions.UnauthorizedRequestException;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
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

    @GetMapping("/find/all")
    public ResponseEntity<List<Page>> findAllPages(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/find")
    public ResponseEntity<Page> findPageById(@RequestParam Long pageId){
        return ResponseEntity.ok(service.findPageById(pageId));
    }

    @PostMapping("/create-page")
    public ResponseEntity<Page> savePage(@RequestBody PageDTO pageDTO){
        Page page = new Page(pageDTO.getPageName(), pageDTO.getPageDescription(), pageDTO.getCreatorId());
        return ResponseEntity.ok(service.savePage(page));
    }

    @PutMapping("/moderators/add")
    public ResponseEntity<?> addModeratorsPage(@RequestParam Long pageId, Long moderatorId){
        Page page = service.findPageById(pageId);
        List<Long> moderatorsIds = page.getModeratorsId();
        userService.findById(moderatorId); // Verificando se moderador existe

        // Verificar se usuário já não é moderador
        if(moderatorsIds.contains(moderatorId))
            throw new BadRequestException("O usuário com id " + moderatorId + " já é moderador.");

        // Adicionar moderador
        moderatorsIds.add(moderatorId);
        page.setModeratorsId(moderatorsIds);
        page.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<Page>(service.savePage(page, false), HttpStatus.OK);
    }

    @DeleteMapping("/moderators/delete")
    public ResponseEntity<?> deleteModeratorsPage(@RequestParam Long pageId, Long moderatorId){
        Page page = service.findPageById(pageId);
        List<Long> moderatorsIds = page.getModeratorsId();
        userService.findById(moderatorId); // Verificando se o usuário existe

        if(!moderatorsIds.contains(moderatorId))
            throw new BadRequestException("O usuário com id " + moderatorId + " não é moderador");

        // Verificar se o número de moderadores é maior que o mínimo
        if(moderatorsIds.size() == 1)
            throw new BadRequestException("O número de moderadores não pode ser menor que 1.");

        // Deletar moderador
        moderatorsIds.remove(moderatorId);
        page.setModeratorsId(moderatorsIds);
        page.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<Page>(service.savePage(page, false), HttpStatus.OK);
    }

    @PutMapping("/new-post")
    public ResponseEntity<?> createPost(@RequestParam Long pageId, @RequestBody PostDTO post){
        // Criando post
        Page page = service.findPageById(pageId);
        User user = userService.findById(post.getUserId()); // Verificando se usuário existe
        Post pagePost = new Post(post.getUserId(), page.getPageName(), post.getPostTitle(), post.getPostBody());

        // Salvando post no banco de dados
        pagePost = postService.savePost(pagePost);

        // Adicionando o post na lista de posts da página
        List<Long> postsId = page.getPostsId();
        postsId.add(pagePost.getId());
        page.setPostsId(postsId);
        service.savePage(page, false);

        return new ResponseEntity<Post>(pagePost, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePage(@RequestParam Long pageId, Long userId){
        Page page = service.findPageById(pageId);
        // Verificando autorização
        if(!page.getModeratorsId().contains(userId))
            throw new UnauthorizedRequestException("User unauthorized");

        // Apagando todos os posts da página
        List<Long> postsIds = page.getPostsId();
        postsIds.forEach(postId -> {
            postService.deletePost(postId);
        });
        service.deletePage(pageId);
        return new ResponseEntity<String>("Página com id "+pageId+" deletada", HttpStatus.OK);
    }

}
