package com.dany.michelladas.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO utilizado para devolver la información completa de un pedido
 * ya registrado en el sistema, tanto al usuario como al administrador.
 *
 * Incluye datos del pedido, productos, totales, fecha y estado.
 *
 * @author Dan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoRespuestaDto {

    /**
     * ID del pedido
     */
    private Long id;

    /**
     * Nombre del usuario que realizó el pedido
     */
    private String nombreUsuario;

    /**
     * Dirección de entrega
     */
    private String direccion;

    /**
     * Metodo de pago usado
     */
    private String metodoPago;

    /**
     * Notas o puntos de referencia
     */
    private String notas;

    /**
     * Estado del pedido (ej: PENDIENTE, ENTREGADO)
     */
    private String estado;

    /**
     * Fecha y hora en que se creó el pedido
     */
    private LocalDateTime fecha;

    /**
     * Total del pedido (suma de subtotales + domicilio si aplica)
     */
    private Double total;

    /**
     * Lista de productos con su información y cantidad
     */
    private List<DetallePedidoRespuestaDto> productos;

    /**
     * DTO que representa un producto dentro de un pedido ya registrado.
     * Incluye nombre, precio y cantidad.
     * <p>
     * @author Dan
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetallePedidoRespuestaDto {

        /**
         * Nombre del producto
         */
        private String nombre;

        /**
         * Precio unitario del producto
         */
        private Double precio;

        /**
         * Cantidad solicitada
         */
        private int cantidad;

    }


}



