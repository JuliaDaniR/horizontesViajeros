package com.empresaTurismo.agenciadeturismo.repositorio;

import com.empresaTurismo.agenciadeturismo.entidades.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaqueteRepository extends JpaRepository<Paquete, Long>{
    
}
