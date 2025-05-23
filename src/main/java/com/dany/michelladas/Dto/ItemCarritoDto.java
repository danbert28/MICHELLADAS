package com.dany.michelladas.Dto;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDto {
        private Long productoId;
        private String nombreProducto;
        private Double precio;
        private Integer cantidad;
        private String imagen;
}


