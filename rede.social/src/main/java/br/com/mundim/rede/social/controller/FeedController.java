package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.entity.Comment;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.service.CommentService;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
import br.com.mundim.rede.social.view.PostView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/feed")
@Api(tags = "Feed")
public class FeedController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private CommentService commentService;

    private List<Post> generateFeedUser(Long id) {
        List<Post> postViews = new ArrayList<>();
        User user = userService.findById(id);

        user.getFollowing().forEach((following) -> {
            String[] type = following.split(" ");
            if (Objects.equals(type[0], "PAGE")) {
                List<Long> postsIds = pageService.findAllPostsFromPage(Long.parseLong(type[1]));
                postsIds.forEach(post -> {
                    Post findedPost = postService.findPostById(post);
                    if (findedPost != null) postViews.add(findedPost);
                });
            }
            if (Objects.equals(type[0], "USER")) {
                List<Post> userPosts = postService.findPostByUserId(Long.parseLong(type[1]));
                postViews.addAll(userPosts);
            }
        });

        // Organizando os posts pela quantidade de likes
        postViews.sort(Comparator.comparing(post -> post.getLikesId().size(), Comparator.nullsLast(Comparator.reverseOrder())));

        return postViews;
    }

    private List<PostView> creatingPostView(List<Post> posts){
        List<PostView> postViews = new ArrayList<>();
        posts.forEach(post -> {
            User user = userService.findById(post.getUserId());
            List<Comment> comments = new ArrayList<>();
            if(post.getCommentsIds() != null){
                post.getCommentsIds().forEach(id -> {
                    comments.add(commentService.findById(id));
                });
            }
            Page page = pageService.findByPagename(post.getPagename());
            if(page != null){
                PostView postView = new PostView(user.getProfilePic(), user.getUsername(), page.getPagePic(), page.getPageName(),
                        post.getPostTitle(), post.getPostBody(), post.getLikesId().size(), post.getDislikesId().size(), comments);
                postViews.add(postView);
            } else {
                PostView postView = new PostView(user.getProfilePic(), user.getUsername(), null, null,
                        post.getPostTitle(), post.getPostBody(), post.getLikesId().size(), post.getDislikesId().size(), comments);
                postViews.add(postView);
            }
        });
        return postViews;
    }

    @GetMapping
    @ApiOperation(value = "Obter feed",
            notes = "Retorna um pageable com uma lista dos posts de pessoas/páginas que o usuário segue ordenados por likes.")
    public PageImpl<PostView> userFeed(
            @ApiParam(value = "Página atual") @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @ApiParam(value = "ID do usuário sendo buscado")@RequestParam Long userId
    ) {
        List<Post> allPosts = generateFeedUser(userId);
        List<PostView> allPostsView = creatingPostView(allPosts);
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allPostsView.size());

        List<PostView> sublist = allPostsView.subList(start, end);
        return new PageImpl<>(sublist, pageable, allPostsView.size());
    }

    @GetMapping("/search")
    @ApiOperation(value = "Obtém uma lista com tudo aquilo que possui a palavra buscada")
    public List<Object> search(@ApiParam("Palavra a ser buscada") String word) {
        List<Object> founded = new ArrayList<>();
        founded.addAll(userService.findByUsernameContaining(word));
        founded.addAll(pageService.findByPageNameContaining(word));
        founded.addAll(postService.findByPostBodyContaining(word));
        founded.addAll(postService.findByPostTitleContaining(word));
        return founded;
    }


}
