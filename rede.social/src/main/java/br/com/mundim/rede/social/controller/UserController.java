package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.UserDTO;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDTO){
        User user = new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getProfilePic());
        return ResponseEntity.ok(service.save(user));
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUserById(@RequestParam Long userId){
        return ResponseEntity.ok().body(service.findById(userId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAllUsers(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId){
        if(service.findById(userId) == null)
            return new ResponseEntity<String>("O usuário com id " + userId + " não existe.", HttpStatus.BAD_REQUEST);
        service.deleteById(userId);
        if(service.findById(userId) == null)
            return new ResponseEntity<String>("O usuário com id " + userId + " foi deletado.", HttpStatus.OK);
        return new ResponseEntity<String>("Não foi possível apagar o usuário com id " + userId + ".", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestParam Long userId){
        User oldUser = service.findById(userId);
        if(oldUser != null){
            if(!Objects.equals(user.getUsername(), oldUser.getUsername()))
                oldUser.setUsername(user.getUsername());
            if(!Objects.equals(user.getEmail(), oldUser.getEmail()))
                oldUser.setEmail(user.getEmail());
            if(!Objects.equals(user.getPassword(), oldUser.getPassword()))
                oldUser.setPassword(user.getPassword());
            if(!Objects.equals(user.getProfilePic(), oldUser.getProfilePic()))
                oldUser.setProfilePic(user.getProfilePic());
            return new ResponseEntity<User>(service.save(oldUser), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Usuário com id " + userId + " não localizado.", HttpStatus.BAD_REQUEST);
    }
}
