package com.gitlab.jspragadeesh.cabbookingapp.configs;

import com.gitlab.jspragadeesh.cabbookingapp.services.PersonDetailsService;
import com.gitlab.jspragadeesh.cabbookingapp.utility.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final PersonDetailsService personDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(PersonDetailsService personDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.personDetailsService = personDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;
        System.out.println("Filter");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try{
                username = this.jwtTokenUtil.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            } catch (ExpiredJwtException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            }
        } else {
            System.out.println("No token");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.personDetailsService.loadUserByUsername(username);
            if(this.jwtTokenUtil.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set Context if valid token
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authenticated (Filter): " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }
        }

        filterChain.doFilter(request, response);
    }
}
