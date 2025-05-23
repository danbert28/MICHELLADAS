package com.dany.michelladas.Service;
import com.dany.michelladas.Entity.Producto;
import org.springframework.stereotype.Service;
import com.dany.michelladas.Dto.ItemCarritoDto;
import org.springframework.web.context.annotation.SessionScope;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CarritoService {

    private final Map<Long, ItemCarritoDto> carrito = new HashMap<>();

    public void agregarProducto(Producto producto) {
        Long id = producto.getId();

        if (carrito.containsKey(id)) {
            ItemCarritoDto existente = carrito.get(id);
            existente.setCantidad(existente.getCantidad() + 1);
        } else {
            ItemCarritoDto item = ItemCarritoDto.builder()
                    .productoId(id)
                    .nombreProducto(producto.getNombre())
                    .precio(producto.getPrecio())
                    .cantidad(1)
                    .imagen(producto.getImagen())
                    .build();
            carrito.put(id, item);
        }
    }

    public Collection<ItemCarritoDto> obtenerItems() {
        return carrito.values();
    }

    public Double getTotal() {
        return carrito.values().stream()
                .mapToDouble(i -> i.getPrecio() * i.getCantidad())
                .sum();
    }

    public void eliminarProducto(Long id) {
        carrito.remove(id);
    }

    public void vaciarCarrito() {
        carrito.clear();
    }

    public void aumentarCantidad(Long id) {
        if (carrito.containsKey(id)) {
            carrito.get(id).setCantidad(carrito.get(id).getCantidad() + 1);
        }
    }

    public void reducirCantidad(Long id) {
        if (carrito.containsKey(id)) {
            ItemCarritoDto item = carrito.get(id);
            int nuevaCantidad = item.getCantidad() - 1;
            if (nuevaCantidad <= 0) {
                carrito.remove(id);
            } else {
                item.setCantidad(nuevaCantidad);
            }
        }
    }

    public int getTotalItems() {
        return carrito.values().stream()
                .mapToInt(ItemCarritoDto::getCantidad)
                .sum();
    }
}

