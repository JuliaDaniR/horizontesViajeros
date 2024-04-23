package com.empresaTurismo.agenciadeturismo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long num_venta;

    @Temporal(TemporalType.DATE)
    private Date fecha_venta;

    private String medio_pago;
    
    private Double comision;

    @ManyToMany
    private List<Usuario> usuario;

    @ManyToOne
    private Servicio servicio;

    @ManyToOne
    private Paquete paqueteTuristico;

    private Double precioTotalVenta;

    public Venta() {
    }

    public Venta(Long num_venta, Date fecha_venta, String medio_pago, Double comision, List<Usuario> usuario, Servicio servicio, Paquete paqueteTuristico, Double precioTotalVenta) {
        this.num_venta = num_venta;
        this.fecha_venta = fecha_venta;
        this.medio_pago = medio_pago;
        this.comision = comision;
        this.usuario = usuario;
        this.servicio = servicio;
        this.paqueteTuristico = paqueteTuristico;
        this.precioTotalVenta = precioTotalVenta;
    }

}
