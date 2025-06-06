package com.dany.michelladas.Security;

import com.dany.michelladas.Security.jwt.JwtAuthFilter;
import com.dany.michelladas.Security.jwt.JwtEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configuración de seguridad para el sistema Michelladas.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtEntryPoint jwtEntryPoint,
                          JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. Autorización de rutas públicas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",             // Página principal
                                "/login",        // Vista login.html
                                "/register",     // Vista register.html
                                "/css/**",       // Estilos
                                "/js/**",        // JS
                                "/imagenes/**",  // Imágenes
                                "/api/usuarios/registro",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin protegido por rol
                        .anyRequest().authenticated() //odo lo demas requiere login (incluye API)
                )

                // 2. Configuración CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/register", "/login") // Ignora CSRF en APIs y login/register
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                // 3. Login con formulario (solo para vistas Thymeleaf)
                .formLogin(form -> form
                        .loginPage("/login")                 // Página personalizada
                        .loginProcessingUrl("/login")        // Acción POST del formulario
                        .successHandler(customSuccessHandler())
                        .failureUrl("/login?error=true")     // Si falla el login
                        .permitAll()
                )

                // 4. Logout para vistas
                .logout(logout -> logout
                        .logoutSuccessUrl("/")               // Después del logout, ir a /
                        .permitAll()
                )

                // 5. Añadir filtro JWT ANTES del UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 6. Gestión de sesión combinada
                .sessionManagement(session -> session
                        // ⚠ IMPORTANTE: Este valor aplica a REST. Las vistas siguen usando sesión por defecto.
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // 7. Manejo de errores JWT
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtEntryPoint)
                );

        return http.build();
    }
}

