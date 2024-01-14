package com.template.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.template.entities.user.CustomUser;
import com.template.entities.user.services.UserService;
import com.template.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestPath = request.getRequestURI().substring(request.getContextPath().length());

//		if (!request.getMethod().equalsIgnoreCase("OPTIONS")
//				&& (!requestPath.equals("/item") && (!request.getMethod().equalsIgnoreCase("GET"))
//						|| (requestPath.equals("/item/me") && (request.getMethod().equalsIgnoreCase("GET"))))) {

		if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {

			String authHeader = request.getHeader("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer "))
				throw new UnauthorizedException("Aggiungere il token all'authorization header!");
			String token = authHeader.substring(7);

			JwtTools.isTokenValid(token);

			String id = JwtTools.extractSubject(token);

			CustomUser userCorrente = userService.findById(id);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCorrente,
					null, userCorrente.getAuthorities());

			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authToken);

			filterChain.doFilter(request, response);

		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}

}
