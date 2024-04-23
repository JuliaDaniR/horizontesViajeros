package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.entidades.Imagen;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.servicio.ImagenService;
import com.empresaTurismo.agenciadeturismo.servicio.ServicioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/imagen")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/listar")
    public String listarImagenes(Model model) {
        List<Imagen> imagenes = imagenService.listarTodos();
        model.addAttribute("imagenes", imagenes);
        return "listaImagenes"; // Ajusta este nombre según el nombre de tu vista
    }

    @PostMapping("/guardar")
    public String guardarImagen(
            @RequestParam("url") String url,
            @RequestParam("servicioId") Long servicioId) {
        
        try {
            imagenService.guardar(url, servicioService.obtenerPorId(servicioId));
            return "redirect:/imagen/listar";
        } catch (MyException e) {
            // Manejo de errores
            return "error"; // Ajusta esta vista de error según tu implementación
        }
    }

    @PostMapping("/actualizar")
    public String actualizarImagen(
            @RequestParam("url") String url,
            @RequestParam("idImagen") Long idImagen,
            @RequestParam("servicioId") Long servicioId) {
        try {
            imagenService.actualizar(url, idImagen, servicioService.obtenerPorId(servicioId));
            return "redirect:/imagen/listar";
        } catch (MyException e) {
            // Manejo de errores
            return "error"; // Ajusta esta vista de error según tu implementación
        }
    }
}
