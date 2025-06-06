package com.dany.michelladas.Service;

import com.dany.michelladas.Dto.PedidoRegistroDto;
import com.dany.michelladas.Dto.PedidoRespuestaDto;
import com.dany.michelladas.Entity.*;
import com.dany.michelladas.Repository.*;
import com.dany.michelladas.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetalleRepository detalleRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo pedido desde un usuario autenticado.
     * @param dto datos del pedido (dirección, metodo de pago, productos)
     * @param user usuario autenticado
     * @return resumen del pedido generado
     */
    public PedidoRespuestaDto crearPedido(PedidoRegistroDto dto, UserDetailsImpl user) {
        if (dto.getProductos() == null || dto.getProductos().isEmpty()) {
            throw new RuntimeException("El pedido debe tener al menos un producto.");
        }

        Usuario cliente = usuarioRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setDireccion(dto.getDireccion());
        pedido.setMetodoPago(dto.getMetodoPago());
        pedido.setNotas(dto.getNotas());
        pedido.setEstado(EstadoPedido.PENDIENTE);



        pedido = pedidoRepository.save(pedido);

        double total = 0.0;

        for (PedidoRegistroDto.DetallePedidoDto item : dto.getProductos()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Detalle detalle = new Detalle();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            total += producto.getPrecio() * item.getCantidad();
            detalleRepository.save(detalle);
        }

        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        return convertirADto(pedido);
    }


    /**
     * Cambia el estado de un pedido (ADMIN).
     *
     * @param idPedido ID del pedido
     * @param nuevoEstado Estado nuevo (validado con Enum)
     * @return pedido actualizado
     */


    public PedidoRespuestaDto actualizarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        try {
            EstadoPedido estadoEnum = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
            pedido.setEstado(estadoEnum);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido. Debe ser uno de: PENDIENTE, PREPARADO, EN_CAMINO, ENTREGADO, CANCELADO");
        }

        pedidoRepository.save(pedido);
        return convertirADto(pedido);
    }


    /**
     * Devuelve todos los pedidos de un usuario autenticado.
     * @param user usuario autenticado
     * @return lista de pedidos del usuario
     */
    public List<PedidoRespuestaDto> obtenerPedidosDeUsuario(UserDetailsImpl user) {
        Usuario cliente = usuarioRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoRepository.findByUsuario(cliente).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve todos los pedidos registrados (ADMIN).
     * @return lista de todos los pedidos
     */
    public List<PedidoRespuestaDto> obtenerTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }


    /**
     * Devuelve los productos detallados de un pedido, si pertenece al usuario autenticado.
     *
     * @param pedidoId ID del pedido
     * @param user usuario autenticado
     * @return lista de productos del pedido
     */
    public List<PedidoRespuestaDto.DetallePedidoRespuestaDto> obtenerDetalleDelPedido(Long pedidoId, UserDetailsImpl user) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .filter(p -> p.getUsuario().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado o no autorizado."));

        return pedido.getDetalle()
                .stream()
                .map(det -> PedidoRespuestaDto.DetallePedidoRespuestaDto.builder()
                        .nombre(det.getProducto().getNombre())
                        .precio(det.getPrecioUnitario())
                        .cantidad(det.getCantidad())
                        .build())
                .toList();
    }
    /**
     * Convierte la entidad Pedido en un DTO de respuesta para el frontend.
     *
     * @param pedido entidad a convertir
     * @return DTO con los datos del pedido
     */
    private PedidoRespuestaDto convertirADto(Pedido pedido) {
        List<PedidoRespuestaDto.DetallePedidoRespuestaDto> productos = pedido.getDetalle()
                .stream()
                .map(d -> PedidoRespuestaDto.DetallePedidoRespuestaDto.builder()
                        .nombre(d.getProducto().getNombre())
                        .precio(d.getPrecioUnitario())
                        .cantidad(d.getCantidad())
                        .build())
                .toList();

        return PedidoRespuestaDto.builder()
                .id(pedido.getId())
                .nombreUsuario(pedido.getUsuario().getNombre())
                .direccion(pedido.getDireccion())
                .metodoPago(pedido.getMetodoPago())
                .notas(pedido.getNotas())
                .estado(pedido.getEstado().name())
                .fecha(pedido.getCreatedAt())
                .total(pedido.getTotal())
                .productos(productos)
                .build();
    }
    /**
     * Devuelve los detalles de un pedido específico para el administrador.
     *
     * @param pedidoId ID del pedido
     * @return lista de detalles del pedido
     */
    public List<PedidoRespuestaDto.DetallePedidoRespuestaDto> obtenerDetalleAdmin(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        return pedido.getDetalle().stream()
                .map(detalle -> new PedidoRespuestaDto.DetallePedidoRespuestaDto(
                        detalle.getProducto().getNombre(),
                        detalle.getPrecioUnitario(),
                        detalle.getCantidad()

                ))
                .collect(Collectors.toList());
    }
}

