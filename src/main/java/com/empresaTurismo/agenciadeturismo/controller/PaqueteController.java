package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.servicio.PaqueteService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/paquete")
public class PaqueteController {
   
    @Autowired
   private PaqueteService paqueteService;
    
     @GetMapping("/registrar")
    public String registrar(ModelMap model) {
     
        return "registro_paquete.html";
    }
    
     @PostMapping("/registro")
    public String registro(
            @RequestParam List<Long> id_servicios,
            @RequestParam Double costo_paquete){
        
        try {
            paqueteService.crearPaquete(id_servicios, costo_paquete);
        } catch (MyException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return "index.html";
    }
}
