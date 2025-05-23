package com.dany.michelladas.Repository;

import com.dany.michelladas.Entity.ERol;
import com.dany.michelladas.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByRolenombre(ERol rolenombre);


}


