package com.empresaTurismo.agenciadeturismo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo_paquete;

    @ManyToMany
    @JoinTable(
            name = "paquete_servicio",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private List<Servicio> lista_servicios_incluidos;

    private Double costo_paquete;

    public Paquete() {
    }

    public Paquete(Long codigo_paquete, List<Servicio> lista_servicios_incluidos, Double costo_paquete) {
        this.codigo_paquete = codigo_paquete;
        this.lista_servicios_incluidos = lista_servicios_incluidos;
        this.costo_paquete = costo_paquete;
    }

}
