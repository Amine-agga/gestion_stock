package com.example.ms_user.controller;

import com.example.ms_user.model.User;
import com.example.ms_user.model.dto.LoginRequest;
import com.example.ms_user.model.dto.LoginResponse;
import com.example.ms_user.model.dto.RegisterRequest;
import com.example.ms_user.model.dto.UpdatedUser;
import com.example.ms_user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController{
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterRequest request){
        userService.createUser(request);
        return ResponseEntity.ok("User created.");
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/user/email/{email}")
    public ResponseEntity<User> getUserbyEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }
    @PatchMapping("/user/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id,@RequestParam String password){
        userService.updatePassword(id,password);
        return ResponseEntity.ok("Password updated successfully.");
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody UpdatedUser updatedUser){
        return ResponseEntity.ok(userService.updateUser(id,updatedUser));
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
