package com.dany.michelladas.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/inicio");
                return;
            } else if (role.equals("ROLE_USUARIO")) {
                response.sendRedirect("/");
                return;
            }
        }

        // Si no tiene un rol conocido, lo mandamos a una página por defecto
        response.sendRedirect("/");
    }
}

