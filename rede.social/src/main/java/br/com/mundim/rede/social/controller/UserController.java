package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.UserDTO;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private PageService pageService;

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
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @RequestParam Long userId){
        User user = service.findById(userId);
        if(user != null){
            if(userDTO.getUsername() != null)
                user.setUsername(userDTO.getUsername());
            if(userDTO.getEmail() != null)
                user.setEmail(userDTO.getEmail());
            if(userDTO.getPassword() != null)
                user.setPassword(userDTO.getPassword());
            if(userDTO.getProfilePic() != null)
                user.setProfilePic(userDTO.getProfilePic());
            user.setUpdatedAt(LocalDateTime.now());
            return new ResponseEntity<User>(service.save(user), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Usuário com id " + userId + " não localizado.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/follow/user")
    public ResponseEntity<?> followUser(@RequestParam Long followedId, Long userId){
        User user = service.findById(userId);
        User followedUser = service.findById(followedId);
        if(user == null)
            return new ResponseEntity<String>("Usuário com id " + userId + " não existe.", HttpStatus.BAD_REQUEST);
        if(service.findById(followedId) == null)
            return new ResponseEntity<String>("Usuário com id " + followedId + " não existe.", HttpStatus.BAD_REQUEST);

        // Adiciona o usuário a lista de seguindo
        List<String> followingList = user.getFollowing();
        List<String> followedList = followedUser.getFollowed();
        String newFollowing = "USER " + followedId;
        String newFollowed = "USER" + userId;
        if(followingList.contains(newFollowing))
            return new ResponseEntity<String>("Usuário com id " + userId + " já segue o usuário com id " + followedId + ".", HttpStatus.BAD_REQUEST);
        followedList.add(newFollowed);
        service.save(followedUser);
        followingList.add(newFollowing);
        return new ResponseEntity<User>(service.save(user), HttpStatus.OK);
    }

    @PutMapping("/follow/page")
    public ResponseEntity<?> followPage(@RequestParam Long followedId, Long userId){
        User user = service.findById(userId);
        Page page = pageService.findPageById(followedId);
        if(user == null)
            return new ResponseEntity<String>("Usuário com id " + userId + " não existe.", HttpStatus.BAD_REQUEST);
        if(pageService.findPageById(followedId) == null)
            return new ResponseEntity<String>("Página com id " + followedId + " não existe.", HttpStatus.BAD_REQUEST);

        // Adiciona a página a lista de seguindo
        List<String> followingList = user.getFollowing();
        List<String> followedList = page.getFollowed();
        String newFollowing = "PAGE " + followedId;
        String newFollowed = "USER" + userId;
        if(followingList.contains(newFollowing))
            return new ResponseEntity<String>("Usuário com id " + userId + " já segue a página com id " + followedId + ".", HttpStatus.BAD_REQUEST);
        followedList.add(newFollowed);
        page.setFollowed(followedList);
        pageService.savePage(page);
        followingList.add(newFollowing);
        user.setFollowing(followingList);
        return new ResponseEntity<User>(service.save(user), HttpStatus.OK);
    }
}
