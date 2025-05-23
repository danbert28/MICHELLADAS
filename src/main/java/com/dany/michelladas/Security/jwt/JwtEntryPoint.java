package com.dany.michelladas.Security.jwt;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Clase que se ejecuta cuando se intenta acceder a una ruta protegida sin estar autenticado.
 * Responde con un error 401 Unauthorized y un mensaje personalizado.
 *
 * Funciona como punto de entrada cuando la autenticación falla (filtro no valida el token).
 *
 * @author Dan
 * @version 1.0
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    /**
     * Este metodo se llama automáticamente cuando una autenticación falla.
     *
     * @param request   solicitud HTTP entrante
     * @param response  respuesta HTTP saliente
     * @param authException excepción lanzada por Spring Security
     * @throws IOException si hay error al escribir la respuesta
     * @throws ServletException si hay error del servlet
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        System.err.println(" Error de autenticación: " + authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Acceso no autorizado. Token inválido o inexistente.\"}");
    }
}

