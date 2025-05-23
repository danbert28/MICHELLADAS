package com.dany.michelladas.Repository;
import com.dany.michelladas.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario según su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un Optional que puede contener el usuario encontrado
     */
    Optional<Usuario> findByEmail(String email);   // El Optional es un objeto contenedor que puede contener o no un valor distinto de nulo.

    /**
     * Verifica si existe un usuario con un correo dado.
     *
     * @param email correo electrónico a validar
     * @return true si ya existe, false si está disponible
     */
    boolean existsByEmail(String email);
}
