package com.empresaTurismo.agenciadeturismo.repositorio;

import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> findByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Usuario buscarPorDni(@Param("dni") Integer dni);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> findByRol(@Param("rol") Rol rol);
    
}
