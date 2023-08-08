package org.studymate.domain.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.auth.dto.KakaoUser;
import org.studymate.domain.auth.request.CreateTesterRequest;
import org.studymate.domain.auth.request.KakaoLoginRequest;
import org.studymate.domain.auth.response.AuthResponse;
import org.studymate.domain.user.UserService;
import org.studymate.domain.user.entity.User;
import org.studymate.global.util.JwtProvieder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final JwtProvieder jwtProvider;
	private final UserService userService;
	private final AuthService authService;

	@PostMapping("/dev")
	public ResponseEntity<?> handleAuthDevelop(@RequestBody CreateTesterRequest createTesterRequest) {
		log.debug("{}", createTesterRequest);

		User found = userService.addTestedOne(createTesterRequest);
		String token = jwtProvider.createToken(found);

		return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
	}


	
	@PostMapping("/kakao")
	public ResponseEntity<?> handleKakaoAuthToken(@RequestBody KakaoLoginRequest loginRequest) {
		KakaoUser kakaoUser = authService.exchangeAccessTokenToKakaoUser(loginRequest.getAccessToken());

		User user = userService.addKakaoAuthedOne(kakaoUser);
		String token = jwtProvider.createToken(user);

		return new ResponseEntity<>(AuthResponse.builder().token(token).user(kakaoUser).isNew(kakaoUser.isNew()).build(), HttpStatus.OK);
	}
	
	
	@GetMapping("/kakao/link")
	public ResponseEntity<?> handleKakaoAuthLink(HttpServletRequest request) {
		String link = authService.createKakaoAuthorizeLink();
		return new ResponseEntity<>(Map.of("link", link), HttpStatus.OK);
	}

	@GetMapping("/kakao/callback")
	public ResponseEntity<?> handleKakaoAuthCallback(String code) {
		log.debug("kakao code = {}", code);
		String accessToken = authService.exchangeKakaoCodeToToken(code);
		
		return new ResponseEntity<>(Map.of("accessToken", accessToken), HttpStatus.OK);
	}
	
	
}
