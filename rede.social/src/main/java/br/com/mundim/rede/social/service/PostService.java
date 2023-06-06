package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.Post;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.exceptions.BadRequestException;
import br.com.mundim.rede.social.repository.PostRepository;
import br.com.mundim.rede.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    public Post savePost(Post post){
        return repository.save(post);
    }

    public Post findPostById(Long id){
        Post post = repository.findById(id).orElse(null);
        if(post == null) throw new BadRequestException("Post with id '"+id+"' does not exists");
        else return post;
    }

    public List<Post> findAllPosts(){
        return repository.findAll();
    }

    public List<Post> findPostByUserId(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user == null) throw new BadRequestException("User with id '"+id+"' does not exists");
        else return repository.findByUserId(id);
    }

    public void deletePost(Long id){
        Post post = repository.findById(id).orElse(null);
        if(post == null) throw new BadRequestException("Post with id '"+id+"' does not exists");


        repository.deleteById(id);
    }

}
