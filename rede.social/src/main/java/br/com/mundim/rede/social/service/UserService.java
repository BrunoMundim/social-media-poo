package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User save(User user){
        return repository.save(user);
    }

    public User findById(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
