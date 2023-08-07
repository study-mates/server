package org.studymate.global.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.studymate.domain.user.entity.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtProvieder {

	@Value("${jwt.secret}")
	private String secret;

	public String createToken(User user) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create().withIssuer("studymate").withSubject(String.valueOf(user.getId())).withIssuedAt(new Date())
				.sign(algorithm);
	}

}
