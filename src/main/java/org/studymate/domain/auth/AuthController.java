package org.studymate.domain.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.auth.dto.SignInRequest;
import org.studymate.domain.auth.dto.SignUpRequest;
import org.studymate.domain.user.UserService;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.util.JwtProvieder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/auth")
public class AuthController {

	private final JwtProvieder jwtProvider;
	private final UserService userService;
	
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> handleAuthDevelop(@RequestBody SignInRequest signInRequest) {
		log.debug("{}", signInRequest);

		User found = userService.getOneByUserId(signInRequest.getUserId());
		String token = jwtProvider.createToken(found);
		
		return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> handleAuthDevelop(@RequestBody SignUpRequest signUpRequest) {
		log.debug("{}", signUpRequest);
		User created = userService.addOne(signUpRequest);
		
		return new ResponseEntity<>(Map.of("created", created), HttpStatus.CREATED);
	}
	
	
}
