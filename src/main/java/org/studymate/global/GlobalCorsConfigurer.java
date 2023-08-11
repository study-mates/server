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
public class GlobalCorsConfigurer implements WebMvcConfigurer {


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "UPDATE", "DELETE", "PUT")
				.allowedHeaders("Authorization", "Content-Type").allowCredentials(false).maxAge(3600);
	}
 

}
