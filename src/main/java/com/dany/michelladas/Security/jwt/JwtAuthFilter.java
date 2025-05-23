package com.dany.michelladas.Security.jwt;
import com.dany.michelladas.Security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro que se ejecuta una vez por petición.
 * Se encarga de validar si el token JWT es válido y, si lo es, autentica al usuario en el contexto de Spring.
 *
 * Este filtro se activa automáticamente gracias a la configuración de Spring Security.
 * Se encarga de interceptar todas las peticiones entrantes que tengan un token JWT en la cabecera.
 *
 * @author Dan
 * @version 1.0
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Metodo principal del filtro que intercepta cada request.
     *
     * @param request     Petición HTTP entrante
     * @param response    Respuesta HTTP
     * @param filterChain Cadena de filtros de Spring Security
     * @throws ServletException en caso de error del servlet
     * @throws IOException      en caso de error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el token del encabezado Authorization
        String token = getTokenFromRequest(request);

        // 2. Validar el token
        if (token != null && jwtUtil.validateToken(token)) {

            // 3. Obtener el email desde el token
            String email = jwtUtil.getUsernameFromToken(token);

            // 4. Cargar al usuario desde la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 5. Crear objeto de autenticación con sus roles
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Registrar al usuario en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 7. Continuar con la petición
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization.
     *
     * @param request la solicitud HTTP entrante
     * @return el token sin el prefijo Bearer, o null si no está presente
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // elimina el "Bearer "
        }

        return null;
    }
}


