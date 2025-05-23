package com.dany.michelladas.Controllers;

import com.dany.michelladas.Dto.PedidoRespuestaDto;
import com.dany.michelladas.Entity.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.dany.michelladas.Repository.PedidoRepository;
import com.dany.michelladas.Repository.ProductoRepository;
import com.dany.michelladas.Security.UserDetailsImpl;
import com.dany.michelladas.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador de vistas del ADMIN en el sistema Michelladas.
 * Permite ver pedidos y productos, actualizar estados y gestionar productos.
 *
 * @author Dan
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final PedidoService pedidoService;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    /**
     * Página de inicio del panel administrador.
     */
    @GetMapping("/inicio")
    public String inicioAdmin(Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        model.addAttribute("nombre", user.getNombre());
        return "admin/inicio";
    }

    /**
     * Vista de pedidos: muestra todos los pedidos para administradores.
     */
    @GetMapping("/pedidos")
    public String verPedidos(Model model) {
        List<PedidoRespuestaDto> pedidos = pedidoService.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "admin/pedidos_admin";
    }

    /**
     * Permite cambiar el estado de un pedido.
     */
    @PostMapping("/pedido/estado")
    public String actualizarEstado(@RequestParam Long pedidoId,
                                   @RequestParam String nuevoEstado) {
        pedidoService.actualizarEstado(pedidoId, nuevoEstado);
        return "redirect:/admin/pedidos";
    }

    /**
     * Devuelve los detalles de un pedido específico para admin (JSON).
     */
    @GetMapping("/pedido/{id}/detalle")
    @ResponseBody
    public ResponseEntity<List<PedidoRespuestaDto.DetallePedidoRespuestaDto>> detallePedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerDetalleAdmin(id));
    }

    /**
     * Vista del menú admin: gestionar productos.
     */
    @GetMapping("/menu")
    public String verMenuAdmin(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("productoNuevo", new Producto());
        return "admin/productos_admin";
    }
    /**
     * Vista para crear producto.
     *
     */
    @GetMapping("/producto/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/nuevo_producto";
    }

    /**
     * Guarda un nuevo producto desde la vista admin.
     */
    @PostMapping("/producto/nuevo")
    public String guardarNuevoProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/admin/menu";
    }

    /**
     * Vista para editar producto.
     */
    @GetMapping("/producto/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "admin/form_producto";
    }

    /**
     * Procesa la edición del producto.
     */
    @PostMapping("/producto/editar/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute Producto productoForm) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        producto.setNombre(productoForm.getNombre());
        producto.setDescripcion(productoForm.getDescripcion());
        producto.setPrecio(productoForm.getPrecio());
        producto.setImagen(productoForm.getImagen());

        productoRepository.save(producto);
        return "redirect:/admin/menu";
    }
}

