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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/feed")
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

    @GetMapping("/posts")
    public PageImpl<PostView> userFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long userId
    ) {
        List<Post> allPosts = generateFeedUser(userId);
        List<PostView> allPostsView = creatingPostView(allPosts);
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allPostsView.size());

        List<PostView> sublist = allPostsView.subList(start, end);
        return new PageImpl<>(sublist, pageable, allPostsView.size());
    }

}
