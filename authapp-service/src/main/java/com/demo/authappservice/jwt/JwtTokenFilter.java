package com.demo.authappservice.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.authappservice.constant.MessageConstants;
import com.demo.authappservice.service.AuthAppService;

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

	@Autowired
	private AuthAppService appService;

	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException, RuntimeException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
/*
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, Origin, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Accept, X-Requested-With, remember-me, Authorization, Username, Role");
*/
		String usernameFromHeader = request.getHeader("Username");

		try {
			final String auth = request.getHeader("Authorization");
			final String token = auth == null || auth.contains("null") ? null : auth.split(" ", 2)[1];
			if (null != token && null != usernameFromHeader && !tokenProvider.isTokenExpired(token)
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = appService.loadUserByUsername(tokenProvider.extractUsername(token));

				if (!(userDetails != null && usernameFromHeader.equalsIgnoreCase(userDetails.getUsername())))
					throw new Exception(userDetails.getUsername() + " " + MessageConstants.JWTUsernameMismatchFound);

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				if (usernamePasswordAuthenticationToken.isAuthenticated()) {
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					if (logRequestedURL(request)) {
						if (userDetails.getUsername() != null) {
							logger.info("JwtTokenFilter : User {} and requested url : {}",
									tokenProvider.extractUsername(token), request.getRequestURL());
						}
					}
				}
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			if (e instanceof ExpiredJwtException) {
				logger.error("Session expired for user {}", usernameFromHeader);
				((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				logger.error("User {} invalid token exception : {} ", usernameFromHeader, e.getMessage());
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
