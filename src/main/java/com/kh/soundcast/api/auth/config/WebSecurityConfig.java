package com.kh.soundcast.api.auth.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.kh.soundcast.api.auth.filter.AuthFilter;
import com.kh.soundcast.api.auth.jwt.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
	
	private final JwtProvider jwtProvider;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		
		http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true); //jwt가 반드시 있어야만 사용가능
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setMaxAge(3600L); //캐싱시간 3600초 (1시간)
				
				return config;
			}
		}) )
		.csrf((csrfConfig) -> csrfConfig.disable())
		.sessionManagement(config->
			config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
		.authorizeHttpRequests((authorizeRequest) -> authorizeRequest
				.requestMatchers("/**").permitAll() // 누구나 이용가능한 url
				.requestMatchers("/**").hasRole("USER") //그외는 user권한이 필요
				//.requestMatcher("/admin/**").hasRole("ADMIN") authority테이블에 ROLE_ADMIN
				.anyRequest().authenticated()
				)
		.addFilterBefore(new AuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
}
