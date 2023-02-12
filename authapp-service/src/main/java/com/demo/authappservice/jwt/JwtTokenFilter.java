package com.demo.authappservice.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JwtTokenProvider tokenProvider;

	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException, RuntimeException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Accept, X-Requested-With, remember-me, Authorization, Username, Role");

		String username = request.getHeader("Username");

		try {
			final String auth = request.getHeader("Authorization");
			final String token = auth == null || auth.contains("null") ? null : auth.split(" ", 2)[1];
			if (null != token && !tokenProvider.isTokenExpired(token)) {
				Authentication authentication = tokenProvider
						.getAuthentication(tokenProvider.getClaimsFromToken(token).getSubject());

				if (authentication.isAuthenticated()) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					if (logRequestedURL(request)) {
						if (username != null) {
							logger.info("JwtTokenFilter : User {} and requested url : {}",
									authentication.getPrincipal().toString(), request.getRequestURL());
						} else {
							logger.info("JwtTokenFilter : User {} and requested url from API: {}",
									authentication.getPrincipal().toString(), request.getRequestURL());
						}
					}
				}
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			if (e instanceof ExpiredJwtException) {
				logger.error("Session expired for user {}", username);
				((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				logger.error("Invalid token for user {} with exception as {} ", username, e.getMessage());
				((HttpServletResponse) res).setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
			return;
		}
		filterChain.doFilter(request, response);
	}

	public boolean logRequestedURL(HttpServletRequest req) {
		boolean result = true;
		if (req.getRequestURL().toString().contains("refreshjwttoken")) {
			result = false;
		}
		return result;
	}
}

