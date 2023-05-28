package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    public Post savePost(Post post){
        return repository.save(post);
    }

    public void deletePost(Long id){
        repository.deleteById(id);
    }

    public Post findPostById(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<Post> findAllPosts(){
        return repository.findAll();
    }

    public List<Post> findPostByUserId(Long id){
        return repository.findByUserId(id);
    }

}
