package com.dany.michelladas.Controllers;
import com.dany.michelladas.Dto.*;
import com.dany.michelladas.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador encargado del registro y autenticación de usuarios.
 * Expone endpoints públicos para login y registro.
 *
 * @author Dan
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param dto datos enviados desde el formulario de registro
     * @return respuesta con los datos del usuario y su token JWT
     */
    @PostMapping("/registro")
    public ResponseEntity<URespuestaDto> registrar(@Valid @RequestBody URegistroDto dto) {
        URespuestaDto respuesta = usuarioService.registrar(dto);
        return ResponseEntity.ok(respuesta);
    }


    /**
     * Endpoint para iniciar sesión de un usuario ya registrado.
     *
     * @param dto datos de login (email y contraseña)
     * @return respuesta con los datos del usuario autenticado y su token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<URespuestaDto> login(@Valid @RequestBody ULoginDto dto) {
        URespuestaDto respuesta = usuarioService.login(dto);
        return ResponseEntity.ok(respuesta);
    }


}


