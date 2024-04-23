package com.empresaTurismo.agenciadeturismo.entidades;

import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id_usuario;
    private String nombre;
    private String apellido;
    private String direccion;
    private String dni;

    @Temporal(TemporalType.DATE)
    private Date fecha_nac;

    private String nacionalidad;
    private Long celular;
    private String email;
    private String password;
    private String cargo;
    private Double sueldo;
    private Boolean estado;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "vendedor")
    private List<Servicio> servicios;
        
    public Usuario() {

    }

    public Usuario(Long id_usuario, String nombre, String apellido, String direccion, String dni, Date fecha_nac, String nacionalidad, Long celular, String email, String password, String cargo, Double sueldo, Boolean estado, Rol rol, List<Servicio> servicios) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nac = fecha_nac;
        this.nacionalidad = nacionalidad;
        this.celular = celular;
        this.email = email;
        this.password = password;
        this.cargo = cargo;
        this.sueldo = sueldo;
        this.estado = estado;
        this.rol = rol;
        this.servicios = servicios;
    }

}
