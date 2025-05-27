package com.dany.michelladas.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.dany.michelladas.Dto.ItemCarritoDto;
import com.dany.michelladas.Dto.PedidoRegistroDto;
import com.dany.michelladas.Dto.PedidoRespuestaDto;
import com.dany.michelladas.Entity.EstadoPedido;
import com.dany.michelladas.Security.UserDetailsImpl;
import com.dany.michelladas.Service.PedidoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controlador para gestionar los pedidos realizados en Michelladas.
 * Incluye creación de pedidos por usuarios autenticados y consulta para usuarios y administradores.
 *
 * - Usuario: puede crear pedidos y consultar los suyos.
 * - Admin: puede ver todos los pedidos realizados.
 *
 * @author Dan
 * @version 1.0
 */
@Controller
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/pedido")
    @PreAuthorize("hasRole('USUARIO')")
    public String mostrarFormulario(Model model, HttpSession session) {
        List<ItemCarritoDto> items = (List<ItemCarritoDto>) session.getAttribute("items");
        double total = (double) session.getAttribute("total");

        model.addAttribute("pedidoRegistroDto", new PedidoRegistroDto());
        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "pedido";
    }


    /**
     * Endpoint para que un usuario registrado cree un nuevo pedido.
     * Se requiere autenticación mediante JWT con rol USER o ADMIN.
     *
     * @param dto datos del pedido enviados por el frontend
     * @param user usuario autenticado extraído del token
     * @return resumen del pedido generado
     */
    @PostMapping("/crear")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<PedidoRespuestaDto> crearPedido(
            @Validated @RequestBody PedidoRegistroDto dto,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        PedidoRespuestaDto respuesta = pedidoService.crearPedido(dto, user);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Endpoint para que el usuario vea sus pedidos anteriores.
     * Solo accesible si está autenticado.
     *
     * @param user usuario autenticado (extraído desde el JWT)
     * @return lista de pedidos realizados por el usuario
     */
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<List<PedidoRespuestaDto>> verMisPedidos(@AuthenticationPrincipal UserDetailsImpl user) {
        List<PedidoRespuestaDto> pedidos = pedidoService.obtenerPedidosDeUsuario(user);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Endpoint para que el administrador vea todos los pedidos.
     * Solo accesible para usuarios con rol ADMIN.
     *
     * @return lista de todos los pedidos realizados en el sistema
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoRespuestaDto>> verTodosLosPedidos() {
        List<PedidoRespuestaDto> pedidos = pedidoService.obtenerTodos();
        return ResponseEntity.ok(pedidos);
    }
    /**
     * Endpoint para que el administrador actualice el estado de los pedidos.
     * Solo accesible para usuarios con rol ADMIN.
     *
     * @return Estado de pedido: PENDIENTE, PREPARANDO, EN_CAMINO, ENTREGADO, CANCELADO
     */
    @GetMapping("/estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> obtenerEstadosDisponibles() {
        List<String> estados = Arrays.stream(EstadoPedido.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(estados);
    }

}

