package org.studymate.global.util;



import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.studymate.domain.user.entity.User;
import org.studymate.global.exception.UnauthorizedException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtProvieder {

	@Value("${jwt.secret}")
	private String secret;

	public String createToken(User user) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create().withIssuer("studymate").withSubject(String.valueOf(user.getId())).withIssuedAt(new Date())
				.sign(algorithm);
	}

	public String verifyToken(String token) {
		
		Algorithm algorithm = Algorithm.HMAC256(secret);
		var verifier = JWT.require(algorithm).build();
		try {
			DecodedJWT decodedJWT = verifier.verify(token);
			
			return decodedJWT.getSubject();
		}catch(Exception e) {
			return null;
		}
	}

}
