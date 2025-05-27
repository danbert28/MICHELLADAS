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
     * Muestra la página de inicio del panel administrativo.
     *
     * @param model el modelo utilizado para enviar datos a la vista
     * @param user el usuario autenticado que ha iniciado sesión
     * @return la vista correspondiente al inicio del panel administrador
     */
    @GetMapping("/inicio")
    public String inicioAdmin(Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        model.addAttribute("nombre", user.getNombre());
        return "admin/inicio";
    }

    /**
     * Muestra todos los pedidos registrados, visible solo para administradores.
     *
     * @param model el modelo utilizado para agregar la lista de pedidos a la vista
     * @return la vista de administración de pedidos
     */
    @GetMapping("/pedidos")
    public String verPedidos(Model model) {
        List<PedidoRespuestaDto> pedidos = pedidoService.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "admin/pedidos_admin";
    }

    /**
     * Actualiza el estado de un pedido específico.
     *
     * @param pedidoId el ID del pedido cuyo estado se desea actualizar
     * @param nuevoEstado el nuevo estado que se asignará al pedido
     * @return redirección a la lista de pedidos actualizada
     */
    @PostMapping("/pedido/estado")
    public String actualizarEstado(@RequestParam Long pedidoId,
                                   @RequestParam String nuevoEstado) {
        pedidoService.actualizarEstado(pedidoId, nuevoEstado);
        return "redirect:/admin/pedidos";
    }

    /**
     * Obtiene el detalle de un pedido específico en formato JSON.
     *
     * @param id el ID del pedido del cual se desean obtener los detalles
     * @return una lista de objetos DTO que representan los detalles del pedido
     */
    @GetMapping("/pedido/{id}/detalle")
    @ResponseBody
    public ResponseEntity<List<PedidoRespuestaDto.DetallePedidoRespuestaDto>> detallePedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerDetalleAdmin(id));
    }

    /**
     * Muestra la vista del menú de administración de productos.
     *
     * @param model el modelo utilizado para pasar los productos actuales y el formulario de nuevo producto
     * @return la vista del menú de productos para administración
     */
    @GetMapping("/menu")
    public String verMenuAdmin(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("productoNuevo", new Producto());
        return "admin/productos_admin";
    }
    /**
     * Muestra el formulario para registrar un nuevo producto.
     *
     * @param model el modelo utilizado para cargar el objeto producto vacío
     * @return la vista del formulario de creación de nuevo producto
     */
    @GetMapping("/producto/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/nuevo_producto";
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param producto el objeto producto obtenido desde el formulario
     * @return redirección a la vista del menú administrativo
     */
    @PostMapping("/producto/nuevo")
    public String guardarNuevoProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/admin/menu";
    }

    /**
     * Muestra el formulario de edición de un producto existente.
     *
     * @param id el ID del producto que se desea editar
     * @param model el modelo utilizado para cargar el producto en el formulario
     * @return la vista del formulario de edición de producto
     */
    @GetMapping("/producto/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "admin/form_producto";
    }

    /**
     * Procesa la actualización de un producto existente.
     *
     * @param id el ID del producto a actualizar
     * @param productoForm los datos actualizados del producto obtenidos del formulario
     * @return redirección a la vista del menú de productos
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

