package com.empresaTurismo.agenciadeturismo.enumerador;

public enum TipoServicio {
    
    AUTO("Alquiler de Auto"),
    TREN("Pasaje de Tren"),
    EXCURSIONES("Excursiones"),
    HOTELES("Hotel por noche"),
    COLECTIVO("Pasaje de Colectivo"),
    EVENTOS("Entrada a eventos"),
    AVION("Pasaje de Avion");
    
    private String descripcion;

    private TipoServicio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}
