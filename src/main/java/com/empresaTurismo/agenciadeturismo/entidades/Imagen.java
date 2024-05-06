package com.empresaTurismo.agenciadeturismo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String mime;

    private String nombre;

    @Column(length = 1000)
    private String url;

    private String rutaImagen; // Nueva columna para almacenar la ruta en el sistema de archivos

    @ManyToOne
    private Servicio servicio;

    public Imagen() {
    }

    public Imagen(Long id, String mime, String nombre, String url, String rutaImagen, Servicio servicio) {
        this.id = id;
        this.mime = mime;
        this.nombre = nombre;
        this.url = url;
        this.rutaImagen = rutaImagen;
        this.servicio = servicio;
    }
    
     // Constructor que acepta un objeto Servicio y una URL de imagen
    public Imagen(Servicio servicio, String url) {
        this.servicio = servicio;
        this.url = url;
    }

    public Imagen(String url) {
        this.url = url;
    }

 
}
