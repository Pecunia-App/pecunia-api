package com.pecunia.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenBlackList tokenBlackList;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenBlackList tokenBlackList) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.tokenBlackList = tokenBlackList;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);

      if (tokenBlackList.isBlacklisted(jwt)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalideted.");
        return;
      }

      String username = jwtService.extractClaims(jwt).getSubject();

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.extractClaims(jwt).getExpiration().after(new Date())) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities()
          );

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
