package com.kh.soundcast.member.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.kh.soundcast.member.jwt.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberFilter extends GenericFilterBean {
	
	private final JwtProvider jwtProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException{

		String token = jwtProvider.resolveToken((HttpServletRequest)request);
		
		log.debug("요청 받은 토큰 - {}", token);

		if(token != null && jwtProvider.validationToken(token)) {
			Authentication authentication = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("검증 완료된 유저 정보 - {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
		chain.doFilter(request, response);
	}
	
}
