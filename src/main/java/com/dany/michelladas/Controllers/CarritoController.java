package com.dany.michelladas.Controllers;
import org.springframework.ui.Model;
import com.dany.michelladas.Entity.Producto;
import com.dany.michelladas.Repository.ProductoRepository;
import com.dany.michelladas.Service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final ProductoRepository productoRepository;
    private final CarritoService carritoService;

    @GetMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        carritoService.agregarProducto(producto);
        return "redirect:/"; // o redirigir con fragmento #productos
    }

    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("total", carritoService.getTotal());
        return "carrito";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        carritoService.eliminarProducto(id);
        return "redirect:/carrito";
    }

    @GetMapping("/vaciar")
    public String vaciar() {
        carritoService.vaciarCarrito();
        return "redirect:/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Long productoId, @RequestParam String accion) {
        if ("add".equals(accion)) {
            carritoService.aumentarCantidad(productoId);
        } else if ("sub".equals(accion)) {
            carritoService.reducirCantidad(productoId);
        }
        return "redirect:/carrito";
    }

    @GetMapping("/contador")
    @ResponseBody
    public int getContador() {
        return carritoService.getTotalItems();
    }
}

