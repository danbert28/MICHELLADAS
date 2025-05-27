package com.dany.michelladas.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor         // Genera el constructor con los paremetros
@Builder     // Con esto, lombok implementa el patron de diseño Builder para construir objetos de esta clase
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Enumeración del nombre del rol (ADMIN, USUARIO) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERol rolenombre;

    // Constructor vacío obligatorio
    public Rol() {}

    // Constructor con enumeración
    public Rol(ERol rolenombre) {
        this.rolenombre = rolenombre;
    }
}
