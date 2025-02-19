package com.example.UrlShortener.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userService.getUsers();

            if (users.size() == 0) {
                throw new Exception();
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not find users", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            Optional<User> existing_user = userService.findUserByEmail(user.getEmail());
            System.out.println(existing_user);
            if(!existing_user.isPresent()){
                User new_user = userService.createUser(user);
                return new ResponseEntity<>(new_user,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("User email address already in use!!",HttpStatus.BAD_REQUEST);
            }

        }
        catch(Exception e){
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> findUserByEmail(@PathVariable String email){
        try{
            Optional<User> user =  userService.findUserByEmail(email);
            if(user.isPresent()) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            throw new Exception();
        }
        catch(Exception e){
            return new ResponseEntity<>("No email address found",HttpStatus.BAD_REQUEST);
        }
    }


}
