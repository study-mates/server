package org.studymate.global.filter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.UnauthorizedException;
import org.studymate.global.util.JwtProvieder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final JwtProvieder jwtProvieder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String authorizationHeader = request.getHeader("Authorization");
		log.debug("token {}", authorizationHeader);
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			System.out.println("??????===================");
			response.sendError(400, "Bearer 형식의 토큰이 아닙니다.");
			return false;
		}

		String token = authorizationHeader.substring(7); //
		String subject = jwtProvieder.verifyToken(token);
		if (subject == null) {
			response.sendError(400, "유효하지 않은 토큰입니다.");
			return false;
		}
		
		request.setAttribute("userId", subject);
		return true;
	}
}
