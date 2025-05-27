package com.dany.michelladas.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor     // Genera el constructor con los paremetros
@NoArgsConstructor      // Genera el constructor sin los parametros
@Builder                // Con esto, lombok implementa el patron de diseño Builder para construir objetos de esta clase
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha de creación del pedido */
    private LocalDateTime createdAt = LocalDateTime.now();

    /** Precio total del pedido */
    private double total;

    /** Metodo de pago */
    private String metodoPago;

    /**direccion de entrega */
    private String direccion;

    /**notas adicionales */
    private String notas;

    /** Estado actual de la orden (pendiente, preparación, en camino, entregado) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Detalle> Detalle = new ArrayList<>();
}
