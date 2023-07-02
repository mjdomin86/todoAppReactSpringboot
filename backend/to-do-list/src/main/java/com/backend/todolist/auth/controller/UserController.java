package com.backend.todolist.auth.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.backend.todolist.auth.jwt.JwtTokenGenerator;
import com.backend.todolist.auth.repository.UserRepository;
import com.backend.todolist.auth.service.UserService;


@RestController
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@ApiResponses(value = {
		@ApiResponse(responseCode="400", description = "Bad Request"),
		@ApiResponse(responseCode="401", description = "Unauthorized"),
		@ApiResponse(responseCode="403", description = "Forbidden"),
		@ApiResponse(responseCode="404", description = "Not Found")
})
public class UserController {	
	final AuthenticationManager authenticationManager;
	
	final PasswordEncoder passwordEncoder;
    
	final JwtTokenGenerator jwtTokenGenerator;
    
	final UserRepository userRepository;
	
	final UserService userService;

	public UserController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator, UserRepository userRepository, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/api/auth/signin")
    public ResponseEntity<UserSigninResponse> signin(@Valid @RequestBody UserSigninRequest userSigninRequest) {
		return new ResponseEntity<>(userService.signin(userSigninRequest), HttpStatus.OK);
    }
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/api/auth/signup")
    public ResponseEntity<UserSignupResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
		return new ResponseEntity<>(userService.signup(userSignupRequest), HttpStatus.OK);
    }
}
