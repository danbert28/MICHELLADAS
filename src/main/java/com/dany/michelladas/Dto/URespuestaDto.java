package com.dany.michelladas.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que devuelve los datos públicos del usuario
 * luego del inicio de sesión o registro exitoso.
 *
 * Incluye: nombre, email, rol y token JWT.
 *
 * Excluye: contraseña, dirección, teléfono, etc.
 *
 * @author Dan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class URespuestaDto {

        /** Nombre del usuario */
        private String nombre;

        /** Correo electrónico del usuario */
        private String email;

        /** Rol del usuario (ej. USER o ADMIN) */
        private String rol;

        /** Token JWT generado al autenticar */
        private String token;


}
