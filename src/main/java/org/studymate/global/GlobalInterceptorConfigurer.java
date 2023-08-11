package org.studymate.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.studymate.global.filter.AuthenticationInterceptor;
import org.studymate.global.util.JwtProvieder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@CrossOrigin("*")
public class GlobalInterceptorConfigurer implements WebMvcConfigurer {

	private final JwtProvieder jwtProvieder;

 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// JWT 토큰 검증을 적용할 URL 패턴 지정
		registry.addInterceptor(new AuthenticationInterceptor(jwtProvieder))
				// 스터디 쪽
				.addPathPatterns("/api/v1/study/**", "/api/v1/invite/**");
	}

}
