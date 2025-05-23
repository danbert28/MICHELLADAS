package com.dany.michelladas.Controllers;
import com.dany.michelladas.Dto.ProductoRespuestaDto;
import com.dany.michelladas.Service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador que maneja las operaciones relacionadas con los productos.
 * Existen rutas públicas (ver productos) y rutas protegidas para admin (crear, editar, eliminar).
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Endpoint público que devuelve todos los productos disponibles.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoRespuestaDto>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    /**
     * Solo accesible para ADMIN. Permite crear un nuevo producto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody ProductoRespuestaDto producto) {
        productoService.crearProducto(producto);
        return ResponseEntity.ok("Producto creado correctamente");
    }

    /**
     * Solo accesible para ADMIN. Permite editar un producto existente.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody ProductoRespuestaDto producto) {
        productoService.editarProducto(id, producto);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    /**
     * Solo accesible para ADMIN. Permite eliminar un producto por ID.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}
