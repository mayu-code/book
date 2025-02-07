package com.transaction.book.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.book.constants.Role;
import com.transaction.book.dto.requestDTO.LoginRequest;
import com.transaction.book.dto.requestDTO.RegistrationRequest;
import com.transaction.book.dto.responseObjects.LoginResponse;
import com.transaction.book.dto.responseObjects.SuccessResponse;
import com.transaction.book.entities.User;
import com.transaction.book.jwtSecurity.CustomUserDetail;
import com.transaction.book.jwtSecurity.JwtProvider;
import com.transaction.book.services.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CustomUserDetail customUserDetail;

    @PostMapping("/registerUser")
    public ResponseEntity<SuccessResponse> registerUser(@RequestBody RegistrationRequest request){
        SuccessResponse response = new SuccessResponse();
        User user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if(user!=null){
            response.setMessage("User Already Present successfully !");
            response.setHttpStatus(HttpStatus.ALREADY_REPORTED);
            response.setStatusCode(200);
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            response.setMessage("password and confirm password does not match !");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setMobileNo(request.getMobileNo());
        newUser.setName(request.getName());
        newUser.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        if(this.userServiceImpl.isEmptyUserTable()){
            newUser.setRole(Role.ADMIN);
            newUser.setApproved(true);
        }
        else{
            newUser.setRole(Role.USER);
        }
        try{
            this.userServiceImpl.registerUser(newUser);
            response.setMessage("user added successfully !");
            response.setHttpStatus(HttpStatus.OK);
            response.setStatusCode(200);
            return ResponseEntity.of(Optional.of(response));
        }catch(Exception e){
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request){
        SuccessResponse response= new SuccessResponse();
        User user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if(user==null){
            response.setMessage("User not present ! wrong mobile No");
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        if(!user.isApproved()){
            response.setMessage("User Not Approved Yet ! please contact to App Owner !");
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        UserDetails userDetails = this.customUserDetail.loadUserByUsername(request.getEmail());
        boolean isPasswordValid = new BCryptPasswordEncoder().matches(request.getPassword(), userDetails.getPassword());
        if(!isPasswordValid){
            response.setMessage("Invalid Password !");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        Authentication authentication = authenticate(user.getEmail(), request.getPassword());
        String role = user.getRole().toString();
        String token = JwtProvider.generateJwt(authentication);

        try{
            LoginResponse response2 = new LoginResponse();
            response2.setRole(role);
            response2.setToken(token);
            response2.setMessage("Login User  Successfully !");
            response2.setHttpStatus(HttpStatus.OK);
            response2.setStatusCode(200);
            return ResponseEntity.of(Optional.of(response2));
        }catch(Exception e){
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    private Authentication authenticate(String email,String password){
        UserDetails userDetails = this.customUserDetail.loadUserByUsername(email);
        if(userDetails==null){
            throw new UsernameNotFoundException("bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,password, userDetails.getAuthorities());
    }
}
