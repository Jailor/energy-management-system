package andrei.assignment1.controllers.filter;

import andrei.assignment1.dtos.UserResponseDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Log
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Value("${custom.user-prefix}")
    private String userPrefix;
    @Value("${custom.user-secret}")
    private String userSecret;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getMethod().equals("OPTIONS")){
            filterChain.doFilter(request, response);
            return;
        }

        String requestedResource = request.getRequestURI();
        if(requestedResource.contains("/ws")){
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            if(token.equals(userSecret)){
                filterChain.doFilter(request, response);
                return;
            }

            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = userPrefix + "user-api/user/access-secure-resource";

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);

                HttpEntity<?> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<String> secureResourceResponse = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<>() {});

                if (secureResourceResponse.getStatusCode() == HttpStatus.OK) {
                    filterChain.doFilter(request, response);
                } else {
                    log.warning("Failed to authenticate request to secure resource");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (RestClientException e) {
                log.severe("Error making authenticated request to secure resource");
                System.err.println(e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
