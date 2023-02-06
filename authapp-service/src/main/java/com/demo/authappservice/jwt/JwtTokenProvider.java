package com.demo.authappservice.jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token-validity-milliseconds}")
	private long validityInMilliseconds;

	public String createToken(String username, String role) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", role);

		Date now = new Date();
		return Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + validityInMilliseconds))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public Boolean validateToken(String token, String user) {
		final String username = extractUsername(token);
		return (username.equalsIgnoreCase(user) && !isTokenExpired(token));
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public List<String> refreshJWTToken(String username, String userrole, String tokenFromHeader) {
		List<String> refreshToken = new ArrayList<String>();
		try {
			long differenceInMinutes = (extractExpiration(tokenFromHeader).getTime() - new Date().getTime()) / 60000;
			if (differenceInMinutes < 15) {
				refreshToken.add(createToken(username, userrole));
				logger.info("JWT token refresh for " + username);
			} else {
				refreshToken.add(tokenFromHeader);
			}
		} catch (Exception e) {
			logger.error("JWT token refresh for " + username + " : Exception - " + e.getMessage());
		}
		return refreshToken;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Autowired
	private UserDetailsService userDetailsService;

	public Authentication getAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
	}
}
