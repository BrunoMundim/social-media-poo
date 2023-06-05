package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.dto.CommentDTO;
import br.com.mundim.rede.social.entity.Comment;
import br.com.mundim.rede.social.repository.CommentRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepostory repostory;

    public Comment save(Comment comment){
        return repostory.save(comment);
    }

    public Comment findById(Long id){
        return repostory.findById(id).orElse(null);
    }

}
