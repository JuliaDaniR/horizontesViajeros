package com.empresaTurismo.agenciadeturismo.entidades;

import com.empresaTurismo.agenciadeturismo.enumerador.TipoServicio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo_servicio;

    private String nombre;
    private String descripcion_breve;
    private String destino_servicio;
    private String pais_destino;

    @Temporal(TemporalType.DATE)
    private Date fecha_servicio;

    private Double costo_servicio;

    @ManyToOne
    private Usuario vendedor;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    private List<Imagen> imagenes;

    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    private Integer visitas;
    
    private Integer cantidad;
    
    public Servicio() {
    }

    public Servicio(Long codigo_servicio, String nombre, String descripcion_breve, String destino_servicio, String pais_destino, Date fecha_servicio, Double costo_servicio, Usuario vendedor, List<Imagen> imagenes, TipoServicio tipoServicio, Integer visitas, Integer cantidad) {
        this.codigo_servicio = codigo_servicio;
        this.nombre = nombre;
        this.descripcion_breve = descripcion_breve;
        this.destino_servicio = destino_servicio;
        this.pais_destino = pais_destino;
        this.fecha_servicio = fecha_servicio;
        this.costo_servicio = costo_servicio;
        this.vendedor = vendedor;
        this.imagenes = imagenes;
        this.tipoServicio = tipoServicio;
        this.visitas = visitas;
        this.cantidad = cantidad;

    }

   

   

    public Servicio(List<Imagen> imagenes, String nombre, String descripcion_breve) {
        this.nombre = nombre;
        this.descripcion_breve = descripcion_breve;
        this.imagenes = imagenes;
    }

     // Método para obtener la URL de la primera imagen
    public String getPrimeraImagenUrl() {
        if (imagenes != null && !imagenes.isEmpty()) {
            return imagenes.get(0).getUrl(); // Suponiendo que Imagen tiene un atributo "url" que representa la URL de la imagen
        }
        return null; // En caso de que la lista de imágenes esté vacía
    }
}
