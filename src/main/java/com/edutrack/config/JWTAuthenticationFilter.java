package com.edutrack.config;

import java.io.IOException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.edutrack.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Extraer token de la solicitud
            Optional<String> jwtTokenOptional = getTokenFromRequest(request);

            if (jwtTokenOptional.isPresent()) {
                String jwtToken = jwtTokenOptional.get();

                // 2. Validar token JWT
                if (JwtUtils.validateToken(jwtToken)) {
                    // 3. Extraer username del token
                    Optional<String> usernameOptional = JwtUtils.getUsernameFromToken(jwtToken);

                    if (usernameOptional.isPresent()) {
                        String username = usernameOptional.get();

                        try {
                            // 4. Cargar detalles del usuario
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            // 5. Crear autenticación
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            // 6. Agregar detalles de la solicitud
                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            // 7. Establecer autenticación en el contexto de seguridad
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                        } catch (UsernameNotFoundException e) {
                            log.error("Usuario no encontrado: {}", username);
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                            return;
                        }
                    }
                }
            }

            // 8. Continuar con la cadena de filtros
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error en el filtro de autenticación JWT", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de autenticación");
        }
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        // 1. Obtener header de autorización
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2. Verificar que el header tenga el formato correcto: Bearer <token>
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }

        // 3. Intentar obtener el token de otros lugares (opcional)
        // Ej: parámetro de consulta, cookie, etc.

        return Optional.empty();
    }
}
