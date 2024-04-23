package com.empresaTurismo.agenciadeturismo.repositorio;

import com.empresaTurismo.agenciadeturismo.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImagenRepository extends JpaRepository<Imagen,Long> {
    
}
