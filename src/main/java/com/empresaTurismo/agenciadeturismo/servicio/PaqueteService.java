package com.empresaTurismo.agenciadeturismo.servicio;

import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.repositorio.IPaqueteRepository;
import com.empresaTurismo.agenciadeturismo.entidades.Paquete;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.repositorio.IServicioRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaqueteService {

    @Autowired
    private IPaqueteRepository paqueteRepo;

    @Autowired
    private IServicioRepository servicioRepo;

    @Transactional
    public Paquete crearPaquete(List<Long> id_servicios,
            Double costo_paquete) throws MyException {

        List<Servicio> servicio = servicioRepo.findAllById(id_servicios);

        validarDatos(id_servicios, costo_paquete);

        Paquete paquete = new Paquete();

        paquete.setLista_servicios_incluidos(servicio);
        paquete.setCosto_paquete(costo_paquete);

        paqueteRepo.save(paquete);
        
        return paquete;
    }

    public List<Paquete> listarPaquetes() {

        List<Paquete> paquete = new ArrayList();
        paquete = paqueteRepo.findAll();

        return paquete;
    }

    @Transactional
    public void modificarPaquete(Long codigo_paquete, List<Long> id_servicios,
            Double costo_paquete) throws MyException {

        Optional<Paquete> respuesta = paqueteRepo.findById(codigo_paquete);

        validarDatos(id_servicios, costo_paquete);
        
        if (respuesta.isPresent()) {
            Paquete paquete = respuesta.get();

            List<Servicio> servicio = servicioRepo.findAllById(id_servicios);
            paquete.setLista_servicios_incluidos(servicio);
            paquete.setCosto_paquete(costo_paquete);
        }

    }

    private void validarDatos(List<Long> id_servicios,
            Double costo_paquete) throws MyException {

        List<Servicio> servicio = servicioRepo.findAllById(id_servicios);

        if (id_servicios == null || servicio.size() < 2) {
            throw new MyException("Debe ingresar dos o mas servicios");
        }
        if (costo_paquete == null) {
            throw new MyException("Debe ingresar el precio del paquete");
        }
    }
    
    public Paquete obtenerPorId(Long paqueteId) {

        Optional<Paquete> paqueteOptional = paqueteRepo.findById(paqueteId);
        return paqueteOptional.orElse(null);

    }
}
