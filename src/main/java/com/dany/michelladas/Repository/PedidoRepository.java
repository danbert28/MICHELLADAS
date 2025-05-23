package com.dany.michelladas.Repository;
import com.dany.michelladas.Entity.Pedido;
import com.dany.michelladas.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario cliente);

}

