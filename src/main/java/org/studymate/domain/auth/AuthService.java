package org.studymate.domain.auth;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.studymate.domain.auth.dto.KakaoUser;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.InternalServerErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
	@Value("${kakao.rest.apikey}")
	private String KAKAO_REST_APIKEY;

	@Value("${kakao.redirect.uri}")
	private String KAKAO_REDIRECT_URI;

	public String createKakaoAuthorizeLink() {
		StringBuilder sb = new StringBuilder("https://kauth.kakao.com/oauth/authorize?response_type=code");
		sb.append("&client_id=");
		sb.append(KAKAO_REST_APIKEY);
		sb.append("&redirect_uri=");
		sb.append(KAKAO_REDIRECT_URI);
		return sb.toString();
	}

	public String exchangeKakaoCodeToToken(String code) {
		String tokenURL = "https://kauth.kakao.com/oauth/token";

		RestTemplate template = new RestTemplate();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", KAKAO_REST_APIKEY);
		body.add("redirect_uri", KAKAO_REDIRECT_URI);
		body.add("code", code);

		RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST,
				URI.create(tokenURL));
		try {
			ResponseEntity<String> response = template.exchange(request, String.class);
			log.debug("result.statuscode = {}", response.getStatusCode());
			log.debug("result.body = {}", response.getBody());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(response.getBody());
			return node.get("access_token").asText();
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw new BadRequestException("인증코드로 발급받을 수 있는 토큰이 없습니다.");
		}
	}

	public KakaoUser exchangeAccessTokenToKakaoUser(String accessToken) {
		String dest = "https://kapi.kakao.com/v2/user/me";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		RestTemplate template = new RestTemplate();

		RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(dest));
		try {
			ResponseEntity<String> response = template.exchange(request, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(response.getBody());
			
			long id = node.get("id").asLong();
			String username = node.get("kakao_account").get("profile").get("nickname").asText();
			String profileImage = node.get("kakao_account").get("profile").get("profile_image_url").asText();

			return new KakaoUser(id, username, profileImage, false, accessToken, false);
		} catch (Exception e) {
			log.debug("exception {}", e.getMessage());
			throw new InternalServerErrorException(e.getMessage());
		}
	}

}
