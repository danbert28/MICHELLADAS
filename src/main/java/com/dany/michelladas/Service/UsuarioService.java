package com.dany.michelladas.Service;
import com.dany.michelladas.Dto.ULoginDto;
import com.dany.michelladas.Dto.URegistroDto;
import com.dany.michelladas.Dto.URespuestaDto;
import com.dany.michelladas.Entity.ERol;
import com.dany.michelladas.Entity.Rol;
import com.dany.michelladas.Entity.Usuario;
import com.dany.michelladas.Repository.RolRepository;
import com.dany.michelladas.Repository.UsuarioRepository;
import com.dany.michelladas.Security.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que maneja la lógica de autenticación y registro de usuarios
 * para el sistema de Michelladas.
 *
 * Incluye generación de tokens y cifrado de contraseñas.
 *
 * @author Dany
 *  @version 1.0
 *
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registra un nuevo usuario y retorna sus datos junto con el token JWT.
     */
    public URespuestaDto registrar(URegistroDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));

        Rol rol = rolRepository.findByRolenombre(ERol.USUARIO)
                .orElseThrow(() -> new RuntimeException("Rol USUARIO no encontrado"));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario.getEmail());

        return new URespuestaDto(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getRolenombre().name(),
                token
        );
    }

    /**
     * Autentica a un usuario existente y retorna sus datos con el token.
     */
    public URespuestaDto login(ULoginDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getContraseña())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getEmail());

        return new URespuestaDto(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getRolenombre().name(),
                token
        );
    }
}


