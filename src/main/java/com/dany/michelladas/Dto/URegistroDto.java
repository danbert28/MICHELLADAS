package com.dany.michelladas.Dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para registrar un nuevo usuario en el sistema Michelladas.
 * Incluye validaciones para asegurar la integridad de los datos.
 *
 * @author Dan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class URegistroDto {

    /** Nombre del usuario */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /** Correo electrónico del usuario */
    @Email(message = "Correo electrónico inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    /**
     * Contraseña segura.
     * Debe tener al menos 8 caracteres, una letra y un número.
     */
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "La contraseña debe tener mínimo 8 caracteres, al menos una letra y un número"
    )
    private String contraseña;

    /** Número de teléfono del usuario */
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    /** Rol del usuario (ej. USER o ADMIN) */
    private String rol;
}

