package com.dany.michelladas.Controllers;
import com.dany.michelladas.Dto.PedidoRespuestaDto;
import com.dany.michelladas.Security.UserDetailsImpl;
import com.dany.michelladas.Service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import com.dany.michelladas.Dto.ItemCarritoDto;
import com.dany.michelladas.Dto.PedidoRegistroDto;
import com.dany.michelladas.Service.PedidoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;
/**
 * Controlador de vistas para gestionar los pedidos del cliente en Michelladas.
 * Se encarga de mostrar el formulario de finalizar compra, procesar el pedido y mostrar la confirmación.
 *
 * Este controlador es exclusivo para vistas HTML con Thymeleaf.
 *
 * @author Dan
 * @version 1.0
 */
@Controller
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoViewController {

    private final PedidoService pedidoService;
    private final CarritoService carritoService;

    /**
     * Muestra el formulario de pedido (finalizar compra), incluyendo el resumen del carrito.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista "pedido"
     */
    @GetMapping
    @PreAuthorize("hasRole('USUARIO')")
    public String mostrarFormulario(Model model) {
        Collection<ItemCarritoDto> items = carritoService.obtenerItems();
        Double total = carritoService.getTotal();

        if (items.isEmpty()) {
            return "redirect:/carrito?error=Carrito+vacío";
        }

        model.addAttribute("pedidoRegistroDto", new PedidoRegistroDto());
        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "pedido"; // Vista: pedido.html
    }

    /**
     * Procesa el formulario de pedido, lo guarda en la base de datos y redirige a la confirmación.
     *
     * @param dto datos del formulario llenado por el usuario
     * @param user usuario autenticado
     * @param redirectAttributes atributos para redirección (pedidoId)
     * @return redirección a la vista de éxito "/pedido/exito"
     */
    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('USUARIO')")
    public String procesarPedido(@ModelAttribute("pedidoRegistroDto") @Valid PedidoRegistroDto dto,
                                 @AuthenticationPrincipal UserDetailsImpl user,
                                 RedirectAttributes redirectAttributes) {

        List<ItemCarritoDto> items = carritoService.obtenerItems().stream().toList();

        if (items.isEmpty()) {
            return "redirect:/carrito?error=Carrito+vacío";
        }

        // Convertir items del carrito a formato DTO
        dto.setProductos(convertirItems(items));

        // Crear el pedido
        PedidoRespuestaDto respuesta = pedidoService.crearPedido(dto, user);

        // Limpiar el carrito después de realizar el pedido
        carritoService.vaciarCarrito();

        // Guardar el ID para mostrarlo en la vista de éxito
        redirectAttributes.addFlashAttribute("pedidoConfirmado", respuesta);

        return "redirect:/pedido/exito";
    }

    /**
     * Convierte los items del carrito a los detalles requeridos por el DTO del pedido.
     *
     * @param items lista de productos en el carrito
     * @return lista de detalles del pedido
     */
    private List<PedidoRegistroDto.DetallePedidoDto> convertirItems(List<ItemCarritoDto> items) {
        return items.stream()
                .map(item -> new PedidoRegistroDto.DetallePedidoDto(item.getProductoId(), item.getCantidad()))
                .toList();
    }

    /**
     * Muestra la vista de éxito de pedido con la información confirmada.
     *
     * @return nombre de la vista "pedido_confirmado"
     */
    @GetMapping("/exito")
    @PreAuthorize("hasRole('USUARIO')")
    public String exitoPedido() {
        return "pedido_confirmado";
    }

    /**
     * Muestra el historial de pedidos del usuario autenticado.
     *
     * @param model modelo para pasar pedidos
     * @param user usuario autenticado
     * @return vista con la lista de pedidos "mis_pedidos"
     */
    @GetMapping("/historial")
    @PreAuthorize("hasRole('USUARIO')")
    public String verHistorialPedidos(Model model,
                                      @AuthenticationPrincipal UserDetailsImpl user) {
        List<PedidoRespuestaDto> pedidos = pedidoService.obtenerPedidosDeUsuario(user);
        model.addAttribute("pedidos", pedidos);
        return "mis_pedidos";
    }

    /**
     * Devuelve los detalles de un pedido específico (opcional, usado con AJAX).
     *
     * @param id ID del pedido
     * @param user usuario autenticado
     * @return lista de detalles del pedido
     */
    @GetMapping("/detalle/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<List<PedidoRespuestaDto.DetallePedidoRespuestaDto>> obtenerDetalle(@PathVariable Long id,
                                                                                             @AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(pedidoService.obtenerDetalleDelPedido(id, user));
    }
}