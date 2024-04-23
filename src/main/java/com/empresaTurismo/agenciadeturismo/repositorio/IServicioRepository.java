package com.empresaTurismo.agenciadeturismo.repositorio;

import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.enumerador.TipoServicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Long> {

    public List<Servicio> findByVendedor(Usuario vendedor);

    @Query("SELECT s FROM Servicio s ORDER BY s.cantidad DESC")
    public List<Servicio> buscarMasVendidos();
    
    @Query("SELECT s FROM Servicio s ORDER BY s.visitas DESC")
    public List<Servicio> buscarMasVisitados();

    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio = :tipoServicio")
    public List<Servicio> buscarPorTipoServicio(@Param("tipoServicio") TipoServicio tipoServicio);

    @Query("SELECT s FROM Servicio s WHERE s.pais_destino = :paisDestino")
    public List<Servicio> buscarPorPaisDestino(@Param("paisDestino") String paisDestino);

    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio = :tipoServicio AND s.pais_destino = :pais_destino ORDER BY s.visitas DESC")
    public List<Servicio> filtrarTipoPaisVisitas(@Param("tipoServicio") TipoServicio tipoServicio, @Param("pais_destino") String pais_destino);

    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio = :tipoServicio AND s.pais_destino = :pais_destino ORDER BY s.cantidad DESC")
    public List<Servicio> filtrarTipoPaisCantidad(@Param("tipoServicio") TipoServicio tipoServicio, @Param("pais_destino") String pais_destino);

    @Query("SELECT s FROM Servicio s WHERE s.pais_destino = :pais_destino ORDER BY s.visitas DESC")
    public List<Servicio> filtrarPaisVisitas(@Param("pais_destino") String pais_destino);

    @Query("SELECT s FROM Servicio s WHERE s.pais_destino = :pais_destino ORDER BY s.cantidad DESC")
    public List<Servicio> filtrarPaisCantidad(@Param("pais_destino") String pais_destino);

    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio = :tipoServicio ORDER BY s.visitas DESC")
    public List<Servicio> filtrarTipoVisitas(@Param("tipoServicio") TipoServicio tipoServicio);

    @Query("SELECT s FROM Servicio s WHERE s.tipoServicio = :tipoServicio ORDER BY s.cantidad DESC")
    public List<Servicio> filtrarTipoCantidad(@Param("tipoServicio") TipoServicio tipoServicio);

    @Query("SELECT s FROM Servicio s WHERE s.pais_destino = :pais_destino")
    public List<Servicio> listarPorPais(@Param("pais_destino") String pais_destino);

}
