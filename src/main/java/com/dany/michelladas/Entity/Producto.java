package com.dany.michelladas.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor     // Genera el constructor con los paremetros
@NoArgsConstructor      // Genera el constructor sin los parametros
@Builder                // Con esto, lombok implementa el patron de diseño Builder para construir objetos de esta clase
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Positive   // Indica que el precio debe ser un numero positivo
    @NotNull(message = "El precio es obligatorio")
    private double precio;


    private String descripcion;
    private String imagen;
}

