package com.kh.soundcast.config;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
		
		//
		http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
//				config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
				config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true); // jwt사용시에만 이용가능
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setMaxAge(3600L); // 캐싱시간 1시간
				
				return config;
			}
		}))
		.csrf((csrfConfig) -> csrfConfig.disable())
		.sessionManagement(config -> 
			config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		)
		.authorizeHttpRequests((authorizeRequest)-> authorizeRequest
//				.requestMatchers("/auth/login/**").permitAll() // 누구나이용가능한 url
//				.requestMatchers("/member/**").permitAll() // 누구나이용가능한 url
//				.requestMatchers("/resource/**").permitAll() // 누구나이용가능한 url
				.requestMatchers("/**").permitAll()
//				.requestMatchers("/**").hasRole("USER") // 그 외는 user권환이 필요
				// .requestMatcher("/admin/**).hasRole("ADMIN") 관리자경우
				.anyRequest().authenticated()
		)
		.addFilterBefore(new AuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class );
		
		return http.build();
	}
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
    	return web -> web.ignoring()
        	.requestMatchers(PathRequest
            	.toStaticResources()
                .atCommonLocations()
                 )
        	.requestMatchers("resource/**");
     }
}
