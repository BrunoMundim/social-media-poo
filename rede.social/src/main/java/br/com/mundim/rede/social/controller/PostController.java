package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.PostDTO;
import br.com.mundim.rede.social.entity.Comment;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.exceptions.UnauthorizedRequestException;
import br.com.mundim.rede.social.service.CommentService;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/post")
@Api(tags = "Post")
public class PostController {

    @Autowired
    private PostService service;

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private CommentService commentService;

    @ApiOperation("Endpoint que cria um novo post para um usuário")
    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO){
        Post post = new Post(postDTO.getUserId(), null, postDTO.getPostTitle(), postDTO.getPostBody());
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.CREATED);
    }

    @ApiOperation("Endpoint que localiza um post pelo ID")
    @GetMapping("/findPost")
    public ResponseEntity<Post> findPostById(@RequestParam Long postId){
        return ResponseEntity.ok(service.findPostById(postId));
    }

    @ApiOperation("Endpoint que localiza todos os posts")
    @GetMapping("/findPost/all")
    public ResponseEntity<List<Post>> findAllPosts(){
        return ResponseEntity.ok(service.findAllPosts());
    }

    @ApiOperation("Endpoint que localiza todos os posts de um usuário pelo seu ID")
    @GetMapping("/findPost/userId")
    public ResponseEntity<List<Post>> findPostByUserId(@RequestParam Long userId){
        return ResponseEntity.ok(service.findPostByUserId(userId));
    }

    @ApiOperation("Endpoint que localiza todos os posts de uma página")
    @GetMapping("/findPost/pageId")
    public ResponseEntity<List<Post>> findPostByPageId(@RequestParam Long pageId){
        List<Long> postsIds = pageService.findAllPostsFromPage(pageId);
        List<Post> posts = new ArrayList<>();
        postsIds.forEach(post -> posts.add(service.findPostById(post)));
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("Endpoint que permite um usuário dar like em um post")
    @PutMapping("/like-post")
    public ResponseEntity<?> likePost(@RequestParam Long postId, Long userId){
        Post post = service.findPostById(postId);
        userService.findById(userId); // Verificando se usuário existe
        List<Long> likesIds = post.getLikesId();
        List<Long> dislikesIds = post.getDislikesId();

        // Remove o like se o usuário já deu like nesse post
        if(likesIds.contains(userId)){
            likesIds.remove(userId);
            post.setLikesId(likesIds);
            post.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
        }
        // Adiciona o like
        if(dislikesIds.contains(userId)){
            dislikesIds.remove(userId);
            post.setDislikesId(dislikesIds);
        }
        likesIds.add(userId);
        post.setLikesId(likesIds);
        post.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que permite o usuário dar dislike em um post")
    @PutMapping("/dislike-post")
    public ResponseEntity<?> dislikePost(@RequestParam Long postId, Long userId){
        Post post = service.findPostById(postId);
        userService.findById(userId); // Verificando se usuário existe
        List<Long> likesIds = post.getLikesId();
        List<Long> dislikesIds = post.getDislikesId();

        // Remove o dislike se o usuário já deu like nesse post
        if(dislikesIds.contains(userId)){
            dislikesIds.remove(userId);
            post.setDislikesId(dislikesIds);
            post.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
        }
        // Adiciona o like
        if(likesIds.contains(userId)){
            likesIds.remove(userId);
            post.setLikesId(likesIds);
        }
        dislikesIds.add(userId);
        post.setDislikesId(dislikesIds);
        post.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<Post>(service.savePost(post), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que permite o usuário comentar um post")
    @PutMapping("/add-comment")
    public ResponseEntity<Comment> addComment(@RequestParam Long postId, Long userId, @RequestBody String commentBody) {
        Post post = service.findPostById(postId);
        Comment comment = new Comment(userId, commentBody);
        commentService.save(comment);
        post.getCommentsIds().add(comment.getId());
        service.savePost(post);
        return ResponseEntity.ok(comment);
    }

    @ApiOperation("Endpoint que deleta um post pelo ID")
    @DeleteMapping("/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam Long postId, Long userId){
        Post post = service.findPostById(postId);
        if(post.getPagename() != null){
            Page page = pageService.findByPagename(post.getPagename());
            if(!page.getModeratorsId().contains(userId) && !Objects.equals(post.getUserId(), userId)){
                throw new UnauthorizedRequestException("User unauthorized");
            } else {
                page.getPostsId().remove(postId);
                pageService.savePage(page, false);
            }
        } else if(!Objects.equals(post.getUserId(), userId))
                throw new UnauthorizedRequestException("User unauthorized");

        service.deletePost(postId);
        return ResponseEntity.ok("Post com o id " + postId + " deletado.");
    }

}
