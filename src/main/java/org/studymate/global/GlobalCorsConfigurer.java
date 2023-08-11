package org.studymate.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.studymate.global.filter.AuthenticationInterceptor;
import org.studymate.global.util.JwtProvieder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCorsConfigurer implements WebMvcConfigurer {


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
				.allowedHeaders("*").maxAge(3600);
	}
 

}
