package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.servicio.EmailService;
import com.empresaTurismo.agenciadeturismo.servicio.ServicioService;
import com.empresaTurismo.agenciadeturismo.servicio.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioService usuService;

    @GetMapping("/")
    public String index(ModelMap modelo) {

        List<String> bgImage = new ArrayList();

        bgImage.add("https://images.pexels.com/photos/15537287/pexels-photo-15537287/free-photo-of-carretera-montanas-arboles-viaje.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        bgImage.add("https://images.pexels.com/photos/1430677/pexels-photo-1430677.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        bgImage.add("https://images.pexels.com/photos/15700862/pexels-photo-15700862/free-photo-of-glaciar-de-svartisen-en-noruega.jpeg");
        bgImage.add("https://images.pexels.com/photos/15900831/pexels-photo-15900831/free-photo-of-paisaje-punto-de-referencia-puente-arboles.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        bgImage.add("https://images.pexels.com/photos/358312/pexels-photo-358312.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        bgImage.add("https://images.pexels.com/photos/6478474/pexels-photo-6478474.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
        bgImage.add("https://images.pexels.com/photos/261204/pexels-photo-261204.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");

        List<String> phrases = new ArrayList();
        phrases.add("Descubre la belleza natural del mundo: ¡Tu próximo destino te espera!");
        phrases.add("Explora paisajes impresionantes y vive nuevas aventuras");
        phrases.add("Disfruta de la tranquilidad de la naturaleza en tu próximo viaje");
        phrases.add("Déjate sorprender por la diversidad de nuestro planeta");
        phrases.add("Conoce lugares únicos y crea recuerdos inolvidables");

        List<Servicio> listaServicios = servicioService.listarServicios();
        List<Servicio> serviciosMasVendidos = servicioService.listarMasVendidos();
        List<Servicio> primeros5ServiciosMasVendidos = new ArrayList();
        List<Servicio> serviciosMasVisitados = servicioService.listarMasVisitados();
        List<Servicio> primeros8ServiciosMasVisitados = new ArrayList<>();
        
        int elementosMostrar = 8;
        int numElementosMostrar = 5;

        System.out.println("lista mas vendidos "+ serviciosMasVendidos);
        if (serviciosMasVendidos.size() >= numElementosMostrar) {
            primeros5ServiciosMasVendidos = serviciosMasVendidos.subList(0, numElementosMostrar);
        }
        
        if (serviciosMasVisitados.size() >= elementosMostrar) {
            primeros8ServiciosMasVisitados = serviciosMasVisitados.subList(0, elementosMostrar);
        }
        System.out.println("lista mas vendidos "+ primeros5ServiciosMasVendidos);
        System.out.println("mas visitados "+primeros8ServiciosMasVisitados);
        
        modelo.addAttribute("listaImagenes", bgImage);
        modelo.addAttribute("listaFrases", phrases);
        modelo.addAttribute("listaServicios", listaServicios);
        modelo.addAttribute("listaServiciosMasVendidos", primeros5ServiciosMasVendidos);
        modelo.addAttribute("listaServiciosMasVisitados", primeros8ServiciosMasVisitados);

        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Tu cuenta no está activa o está en revisión.");
        }

        return "login.html";
    }

    @PostMapping("/loginchek")
    public String login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {

        //guarda el usuario en la sesión
        Usuario usuario = (Usuario) usuService.loadUserByUsername(username);
        request.getSession().setAttribute("usuario", usuario);

        // Redirecciona a la página principal o a donde sea necesario
        return "redirect:/";
    }

    @PostMapping("/contactar")
    public String contactar(@RequestParam String contactoNombre, @RequestParam String contactoEmail, @RequestParam String contactoMensaje, ModelMap modelo) {
        // En lugar de enviar el correo, agregamos el mensaje de agradecimiento al modelo
        modelo.put("contactoNombre", contactoNombre);
        modelo.put("contactoEmail", contactoEmail);
        modelo.put("contactoMensaje", contactoMensaje);
        modelo.put("mostrarAgradecimiento", true); // Indicar que se debe mostrar el mensaje de agradecimiento
        // Devolver la vista con el mensaje de agradecimiento
        return "index.html"; // Supongo que la vista donde se muestra el formulario es index.html
    }

}
