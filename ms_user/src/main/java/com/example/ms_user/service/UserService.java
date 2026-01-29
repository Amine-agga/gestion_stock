package com.example.ms_user.service;

import com.example.ms_user.exception.EmailAlreadyUsedException;
import com.example.ms_user.exception.UserNotFoundException;
import com.example.ms_user.model.Role;
import com.example.ms_user.model.User;
import com.example.ms_user.model.dto.LoginRequest;
import com.example.ms_user.model.dto.LoginResponse;
import com.example.ms_user.model.dto.RegisterRequest;
import com.example.ms_user.model.dto.UpdatedUser;
import com.example.ms_user.repository.UserRepository;
import com.example.ms_user.security.JwtUtil;
import com.example.ms_user.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Transactional
    public User createUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())) throw new EmailAlreadyUsedException("Email already exists.");
        User user = new User();
        user.setPrenom(request.getPrenom());
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if(request.getRole() == null) user.setRole(Role.ROLE_MAGASINIER);
        else user.setRole(request.getRole());
        user.setEnabled(true);
        return userRepository.save(user);
    }
    @Transactional
    public LoginResponse register(RegisterRequest request){
        User user = createUser(request);
        String token = jwtUtil.generateToken(
               user.getEmail(),
                user.getRole(),
                user.getNom(),
                user.getPrenom()
        );
        return new LoginResponse(
                token,
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getRole()
                );
    }
    @Transactional
    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtil.generateToken(
                userDetails.getEmail(),
                Role.valueOf(userDetails.getRole()),
                userDetails.getNom(),
                userDetails.getPrenom()
        );
        return new LoginResponse(
                token,
                userDetails.getNom(),
                userDetails.getPrenom(),
                userDetails.getEmail(),
                Role.valueOf(userDetails.getRole())
        );
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found."));
    }
    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
    }
    @Transactional
    public User updateUser(Long id, UpdatedUser updatedUser){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
        user.setNom(updatedUser.getNom());
        user.setPrenom(updatedUser.getPrenom());
        if(!user.getEmail().equals(updatedUser.getEmail())){
            Optional<User> existsByEmail = userRepository.findByEmail(updatedUser.getEmail());
            if(existsByEmail.isPresent() && !existsByEmail.get().getId().equals(id)) throw new EmailAlreadyUsedException("Email already exists.");
            user.setEmail(updatedUser.getEmail());
        }
        return userRepository.save(user);
    }
    @Transactional
    public void updatePassword(Long id,String password){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
        userRepository.delete(user);
    }
}
