package com.dany.michelladas.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dany.michelladas.Entity.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación personalizada de UserDetails que adapta un UsuarioEntity
 * a un objeto compatible con Spring Security.
 *
 * Esta clase se utiliza internamente por Spring para autenticar y autorizar usuarios,
 * especialmente al validar credenciales y asignar roles.
 *
 * @author Dan
 * @version 1.0
 */
@Data
public class UserDetailsImpl implements UserDetails {

    /**
     * Identificador del usuario.
     */
    private Long id;

    /**
     * Nombre del usuario (puede ser mostrado en UI o logs).
     */
    private String nombre;

    /**
     * Correo electrónico del usuario (utilizado como nombre de usuario).
     */
    private String email;

    /**
     * Contraseña cifrada del usuario (no se expone al frontend).
     */
    @JsonIgnore
    private String contraseña;

    /**
     * Lista de roles o permisos asociados al usuario.
     * Utilizado por Spring Security para autorizar accesos.
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor principal utilizado para crear una instancia personalizada de UserDetails.
     *
     * @param id           ID del usuario
     * @param nombre       Nombre del usuario
     * @param email        Email (username)
     * @param contraseña   Contraseña encriptada
     * @param authorities  Lista de roles del usuario
     */
    public UserDetailsImpl(Long id, String nombre, String email, String contraseña,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.authorities = authorities;
    }

    /**
     * Convierte un objeto UsuarioEntity a una instancia de UserDetailsImpl
     * que puede ser utilizada por Spring Security.
     *
     * @param usuario entidad del usuario desde la base de datos
     * @return objeto UserDetailsImpl compatible con Spring Security
     */
    public static UserDetailsImpl build(Usuario usuario) {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getRolenombre()); // Ej: ROLE_ADMIN

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContraseña(),
                Collections.singletonList(authority)
        );
    }

    /**
     * Devuelve las autoridades o roles del usuario.
     *
     * @return lista de roles como objetos GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Devuelve la contraseña encriptada.
     *
     * @return contraseña cifrada
     */
    @Override
    public String getPassword() {
        return contraseña;
    }

    /**
     * Devuelve el identificador de usuario (aquí usamos el email).
     *
     * @return correo electrónico del usuario
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica si la cuenta no ha expirado.
     *
     * @return true si la cuenta sigue activa
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     *
     * @return true si no está bloqueada
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales no han expirado.
     *
     * @return true si las credenciales son válidas
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
