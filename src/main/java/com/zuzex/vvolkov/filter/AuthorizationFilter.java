package com.zuzex.vvolkov.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        if (request.getServletPath().equals("/login") ||
                    request.getServletPath().equals("/user/token/refresh"))
        {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    TokenUtils.getInstance().verifiedAccessToken(authorizationHeader);

                    log.info("token is verified");

                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("Error in authorization filter: {}", e.getMessage());

                    response.setHeader("error", e.getMessage());
                    response.setStatus(UNAUTHORIZED.value());

                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            new ResponseMapper(UNAUTHORIZED.value(), "error logging", e.getMessage()));
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
