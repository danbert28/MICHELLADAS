package com.dany.michelladas.Dto;
import lombok.*;

/**
 * DTO utilizado para recibir y devolver los datos necesarios para
 * registrar, consultar o modificar productos en el sistema Michelladas.
 *
 * Se utiliza principalmente en las operaciones controladas por el ADMINISTRADOR.
 *
 * Incluye: nombre, precio, descripción e imagen.
 * Excluye: ID interno del producto (se maneja en backend).
 *
 * @author Dan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoRespuestaDto {
    private Long id;
    /** Nombre del producto */
    private String nombre;

    /** Precio unitario del producto */
    private Double precio;

    /** Descripción del producto */
    private String descripcion;

    /** URL o nombre del archivo de imagen */
    private String imagen;
}


