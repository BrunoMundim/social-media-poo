package br.com.mundim.rede.social.service;

import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.exceptions.BadRequestException;
import br.com.mundim.rede.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User save(User user){
        if(repository.findUserByUsername(user.getUsername()) != null)
            throw new BadRequestException("Username already in use");
        return repository.save(user);
    }

    public User save(User user, boolean usernameChanged){
        if(usernameChanged && repository.findUserByUsername(user.getUsername()) != null)
            throw new BadRequestException("Username already in use");
        else return repository.save(user);
    }

    public User findById(Long id){
        User user = repository.findById(id).orElse(null);
        if(user == null) throw new BadRequestException("User with id '" + id + "' does not exist");
        else return user;
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public List<User> findByUsernameContaining(String word){
        return repository.findByUsernameContaining(word);
    }

    public User findByUsername(String username){
        User user = repository.findUserByUsername(username);
        if(user == null) throw new BadRequestException("User with username '" + username + "' does not exist");
        return user;
    }

    public void deleteById(Long id){
        User user = repository.findById(id).orElse(null);
        if(user == null) throw new BadRequestException("User with id '" + id + "' does not exist");
        else repository.deleteById(id);
    }
}
