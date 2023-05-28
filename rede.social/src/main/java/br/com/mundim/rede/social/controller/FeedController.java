package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.PostService;
import br.com.mundim.rede.social.service.UserService;
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

    private List<Post> generateFeedUser(Long id){
        List<Post> posts = new ArrayList<>();
        User user = userService.findById(id);
        user.getFollowing().forEach((following) -> {
            String[] type = following.split(" ");
            if(Objects.equals(type[0], "PAGE")){
                List<Long> postsIds = pageService.findAllPostsFromPage(Long.parseLong(type[1]));
                postsIds.forEach(post -> posts.add(postService.findPostById(post)));
            }
            if(Objects.equals(type[0], "USER")){
                List<Post> userPosts = postService.findPostByUserId(Long.parseLong(type[1]));
                posts.addAll(userPosts);
            }
        });
        posts.sort(Comparator.comparing(Post::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return posts;
    }

    @GetMapping("/posts")
    public PageImpl<Post> userFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long userId
    ) {
        List<Post> allPosts = generateFeedUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allPosts.size());

        List<Post> sublist = allPosts.subList(start, end);
        return new PageImpl<>(sublist, pageable, allPosts.size());
    }

}
