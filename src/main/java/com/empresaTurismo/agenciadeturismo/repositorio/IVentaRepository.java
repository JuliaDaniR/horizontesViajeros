package com.empresaTurismo.agenciadeturismo.repositorio;

import com.empresaTurismo.agenciadeturismo.entidades.Venta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVentaRepository extends JpaRepository<Venta,Long>{

     @Query("SELECT v FROM Venta v WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> buscarPorFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    
}
