package com.dany.michelladas.Service;
import com.dany.michelladas.Dto.ProductoRespuestaDto;
import com.dany.michelladas.Entity.Producto;
import com.dany.michelladas.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import com.dany.michelladas.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio relacionada con productos.
 * Provee funcionalidades como listar, crear, actualizar y eliminar productos.
 *
 * @author Dan
 * @version 1.0
 */
@Service
@Transactional
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Lista todos los productos en formato DTO.
     *
     * @return lista de productos con nombre, descripción, precio e imagen
     */
    public List<ProductoRespuestaDto> listarProductos() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param dto los datos del nuevo producto
     */
    public void crearProducto(ProductoRespuestaDto dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagen(dto.getImagen());

        productoRepository.save(producto);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id  ID del producto a actualizar
     * @param dto datos nuevos del producto
     */
    public void editarProducto(Long id, ProductoRespuestaDto dto) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setImagen(dto.getImagen());

            productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un producto de la base de datos.
     *
     * @param id ID del producto a eliminar
     */
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No existe producto con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    /**
     * Convierte una entidad Producto a su DTO de respuesta.
     */
    private ProductoRespuestaDto convertirADto(Producto producto) {
        return new ProductoRespuestaDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion(),
                producto.getImagen()
        );
    }
}
