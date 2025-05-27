package com.dany.michelladas.Controllers;
import org.springframework.ui.Model;
import com.dany.michelladas.Entity.Producto;
import com.dany.michelladas.Repository.ProductoRepository;
import com.dany.michelladas.Service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador que maneja las operaciones relacionadas con el carrito de compras.
 * @author Dan
 * @version 1.0
 */
@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final ProductoRepository productoRepository;
    private final CarritoService carritoService;

    /**
     * Agrega un producto al carrito según su ID.
     *
     * @param id el ID del producto a agregar
     * @return redirección a la página principal tras agregar el producto
     */
    @GetMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        carritoService.agregarProducto(producto);
        return "redirect:/"; // o redirigir con fragmento #productos
    }

    /**
     * Muestra la vista del carrito con los productos seleccionados y el total acumulado.
     *
     * @param model el modelo que contiene los datos del carrito y el total a pagar
     * @return la vista del carrito de compras
     */
    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("total", carritoService.getTotal());
        return "carrito";
    }

    /**
     * Elimina un producto del carrito usando su ID.
     *
     * @param id el ID del producto a eliminar del carrito
     * @return redirección a la vista del carrito actualizada
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        carritoService.eliminarProducto(id);
        return "redirect:/carrito";
    }

    /**
     * Vacía completamente el contenido del carrito de compras.
     *
     * @return redirección a la vista del carrito vacío
     */
    @GetMapping("/vaciar")
    public String vaciar() {
        carritoService.vaciarCarrito();
        return "redirect:/carrito";
    }

    /**
     * Actualiza la cantidad de un producto en el carrito.
     *
     * @param productoId el ID del producto cuya cantidad se desea modificar
     * @param accion la acción a realizar: "add" para aumentar o "sub" para reducir
     * @return redirección a la vista del carrito actualizada
     */
    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Long productoId, @RequestParam String accion) {
        if ("add".equals(accion)) {
            carritoService.aumentarCantidad(productoId);
        } else if ("sub".equals(accion)) {
            carritoService.reducirCantidad(productoId);
        }
        return "redirect:/carrito";
    }

    /**
     * Obtiene la cantidad total de ítems actualmente en el carrito.
     *
     * @return el número total de productos en el carrito
     */
    @GetMapping("/contador")
    @ResponseBody
    public int getContador() {
        return carritoService.getTotalItems();
    }
}

