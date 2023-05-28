package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.PostDTO;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService service;

    @Autowired
    private UserService userService;

    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO){
        Post post = new Post(postDTO.getUserId(), postDTO.getPostTitle(), postDTO.getPostBody());
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.CREATED);
    }

    @DeleteMapping("/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam Long postId){
        if(service.findPostById(postId) == null){
            return ResponseEntity.badRequest().body("Não existe um post com o id " + postId + ".");
        } else {
            service.deletePost(postId);
            if(service.findPostById(postId) == null){
                return ResponseEntity.ok("Post com o id " + postId + " deletado.");
            } else {
                return ResponseEntity.ok("Não foi possível deletar o post com o id " + postId + ".");
            }
        }
    }

    @GetMapping("/findPost")
    public ResponseEntity<Post> findPostById(@RequestParam Long postId){
        return ResponseEntity.ok(service.findPostById(postId));
    }

    @GetMapping("/findPost/all")
    public ResponseEntity<List<Post>> findAllPosts(){
        return ResponseEntity.ok(service.findAllPosts());
    }

    @PutMapping("/like-post")
    public ResponseEntity<?> likePost(@RequestParam Long postId, Long userId){
        Post post = service.findPostById(postId);
        List<Long> likesIds = post.getLikesId();
        List<Long> dislikesIds = post.getDislikesId();
        if(userService.findById(userId) == null)
            return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST);
        if(service.findPostById(postId) == null)
            return new ResponseEntity<String>("Post não existe", HttpStatus.BAD_REQUEST);
        // Remove o like se o usuário já deu like nesse post
        if(likesIds.contains(userId)){
            likesIds.remove(userId);
            post.setLikesId(likesIds);
            return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
        }
        // Adiciona o like
        if(dislikesIds.contains(userId)){
            dislikesIds.remove(userId);
            post.setDislikesId(dislikesIds);
        }
        likesIds.add(userId);
        post.setLikesId(likesIds);
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
    }

    @PutMapping("/dislike-post")
    public ResponseEntity<?> dislikePost(@RequestParam Long postId, Long userId){
        Post post = service.findPostById(postId);
        List<Long> likesIds = post.getLikesId();
        List<Long> dislikesIds = post.getDislikesId();
        if(userService.findById(userId) == null)
            return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST);
        if(service.findPostById(postId) == null)
            return new ResponseEntity<String>("Post não existe", HttpStatus.BAD_REQUEST);
        // Remove o dislike se o usuário já deu like nesse post
        if(dislikesIds.contains(userId)){
            dislikesIds.remove(userId);
            post.setDislikesId(dislikesIds);
            return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
        }
        // Adiciona o like
        if(likesIds.contains(userId)){
            likesIds.remove(userId);
            post.setLikesId(likesIds);
        }
        dislikesIds.add(userId);
        post.setDislikesId(dislikesIds);
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
    }

}
