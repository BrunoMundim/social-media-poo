package br.com.mundim.rede.social.controller;

import br.com.mundim.rede.social.dto.UserDTO;
import br.com.mundim.rede.social.entity.Page;
import br.com.mundim.rede.social.entity.User;
import br.com.mundim.rede.social.exceptions.BadRequestException;
import br.com.mundim.rede.social.service.PageService;
import br.com.mundim.rede.social.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
@Api(tags = "User")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private PageService pageService;

    @ApiOperation("Endpoint que mostra uma lista com todos os usuários")
    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAllUsers(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @ApiOperation("Endpoint que mostra um usuário a partir do ID")
    @GetMapping("/find")
    public ResponseEntity<User> findUserById(@RequestParam Long userId){
        return ResponseEntity.ok().body(service.findById(userId));
    }

    @ApiOperation("Endpoint que busca um usuário pelo username")
    @GetMapping("/find/username")
    public ResponseEntity<User> findUserByUsername(@RequestParam String username){
        return ResponseEntity.ok().body(service.findByUsername(username));
    }

    @ApiOperation("Endpoint que cria um usuário")
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws BadRequestException {
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getProfilePic());
        return new ResponseEntity<User>(service.save(user), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que atualiza as informações de um usuário")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @RequestParam Long userId){
        User user = service.findById(userId);
        boolean usernameChanged = false;
        if(userDTO.getUsername() != null){
            user.setUsername(userDTO.getUsername());
            usernameChanged = true;
        }
        if(userDTO.getPassword() != null)
            user.setPassword(userDTO.getPassword());
        if(userDTO.getProfilePic() != null)
            user.setProfilePic(userDTO.getProfilePic());
        user.setUpdatedAt(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<User>(service.save(user, usernameChanged), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que faz um usuário seguir outro usuário")
    @PutMapping("/follow/user")
    public ResponseEntity<?> followUser(@ApiParam("ID do usuário a ser seguido") @RequestParam Long followedId,
                                        @ApiParam("ID do usuário que irá seguir")@RequestParam Long userId){
        User user = service.findById(userId);
        User followedUser = service.findById(followedId);

        // Cria as Strings para serem adicionadas nas listas de seguidores
        List<String> followingList = user.getFollowing();
        List<String> followedList = followedUser.getFollowed();
        String newFollowing = "USER " + followedId;
        String newFollowed = "USER" + userId;

        // Verifica se já segue e deixa de seguir, caso contrário segue
        if(followingList.contains(newFollowing)){
            followingList.remove(newFollowing);
            followedList.remove(newFollowed);
        }
        else{
            followedList.add(newFollowed);
            followingList.add(newFollowing);
        }

        // Salva as alterações
        service.save(followedUser, false);
        return new ResponseEntity<User>(service.save(user, false), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que faz um usuário seguir uma página")
    @PutMapping("/follow/page")
    public ResponseEntity<?> followPage(@ApiParam("ID da página a ser seguida") @RequestParam Long followedId,
                                        @ApiParam("ID do usuário que irá seguir") @RequestParam Long userId){
        User user = service.findById(userId);
        Page page = pageService.findPageById(followedId);

        // Cria as Strings para serem adicionadas nas listas de seguidores
        List<String> followingList = user.getFollowing();
        List<String> followedList = page.getFollowed();
        String newFollowing = "PAGE " + followedId;
        String newFollowed = "USER" + userId;

        if(followingList.contains(newFollowing)){
            followingList.remove(newFollowing);
            followedList.remove(newFollowed);
        } else {
            followedList.add(newFollowed);
            page.setFollowed(followedList);
            followingList.add(newFollowing);
            user.setFollowing(followingList);
        }

        pageService.savePage(page, false);
        return new ResponseEntity<User>(service.save(user, false), HttpStatus.OK);
    }

    @ApiOperation("Endpoint que deleta um usuário")
    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam Long userId){
        service.deleteById(userId);
    }
}
