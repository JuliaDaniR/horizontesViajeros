package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.entidades.Imagen;
import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.enumerador.TipoServicio;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.servicio.ImagenService;
import com.empresaTurismo.agenciadeturismo.servicio.ServicioService;
import com.empresaTurismo.agenciadeturismo.servicio.UsuarioService;
import com.empresaTurismo.agenciadeturismo.servicio.VentaService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicio")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/registrar")
    public String registrar(ModelMap model, HttpSession session) {
        // Obtener el usuario de la sesión
        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuariosession");

        List<String> paises = new ArrayList();
        paises.addAll(List.of(
                "Afganistán", "Albania", "Alemania", "Andorra", "Angola", "Antigua y Barbuda", "Arabia Saudita",
                "Argelia", "Argentina", "Armenia", "Australia", "Austria", "Azerbaiyán", "Bahamas", "Bangladés",
                "Barbados", "Baréin", "Bélgica", "Belice", "Benín", "Bielorrusia", "Birmania", "Bolivia",
                "Bosnia y Herzegovina", "Botsuana", "Brasil", "Brunéi", "Bulgaria", "Burkina Faso", "Burundi",
                "Bután", "Cabo Verde", "Camboya", "Camerún", "Canadá", "Catar", "Chad", "Chile", "China", "Chipre",
                "Ciudad del Vaticano", "Colombia", "Comoras", "Corea del Norte", "Corea del Sur", "Costa de Marfil",
                "Costa Rica", "Croacia", "Cuba", "Dinamarca", "Dominica", "Ecuador", "Egipto", "El Salvador",
                "Emiratos Árabes Unidos", "Eritrea", "Eslovaquia", "Eslovenia", "España", "Estados Unidos", "Estonia",
                "Etiopía", "Filipinas", "Finlandia", "Fiyi", "Francia", "Gabón", "Gambia", "Georgia", "Ghana", "Granada",
                "Grecia", "Guatemala", "Guyana", "Guinea", "Guinea Ecuatorial", "Guinea-Bisáu", "Haití", "Honduras",
                "Hungría", "India", "Indonesia", "Irak", "Irán", "Irlanda", "Islandia", "Islas Marshall", "Islas Salomón",
                "Israel", "Italia", "Jamaica", "Japón", "Jordania", "Kazajistán", "Kenia", "Kirguistán", "Kiribati",
                "Kuwait", "Laos", "Lesoto", "Letonia", "Líbano", "Liberia", "Libia", "Liechtenstein", "Lituania",
                "Luxemburgo", "Macedonia del Norte", "Madagascar", "Malasia", "Malaui", "Maldivas", "Malí", "Malta",
                "Marruecos", "Mauricio", "Mauritania", "México", "Micronesia", "Moldavia", "Mónaco", "Mongolia",
                "Montenegro", "Mozambique", "Namibia", "Nauru", "Nepal", "Nicaragua", "Níger", "Nigeria", "Noruega",
                "Nueva Zelanda", "Omán", "Países Bajos", "Pakistán", "Palaos", "Panamá", "Papúa Nueva Guinea",
                "Paraguay", "Perú", "Polonia", "Portugal", "Reino Unido", "República Centroafricana", "República Checa",
                "República del Congo", "República Democrática del Congo", "República Dominicana", "Ruanda", "Rumanía",
                "Rusia", "Samoa", "San Cristóbal y Nieves", "San Marino", "San Vicente y las Granadinas", "Santa Lucía",
                "Santo Tomé y Príncipe", "Senegal", "Serbia", "Seychelles", "Sierra Leona", "Singapur", "Siria",
                "Somalia", "Sri Lanka", "Suazilandia", "Sudáfrica", "Sudán", "Sudán del Sur", "Suecia", "Suiza",
                "Surinam", "Tailandia", "Tanzania", "Tayikistán", "Timor Oriental", "Togo", "Tonga", "Trinidad y Tobago",
                "Túnez", "Turkmenistán", "Turquía", "Tuvalu", "Ucrania", "Uganda", "Uruguay", "Uzbekistán", "Vanuatu",
                "Venezuela", "Vietnam", "Yemen", "Yibuti", "Zambia", "Zimbabue"
        ));

        // Verificar si el usuario está en sesión
        if (usuarioEnSesion != null) {
            // Si está en sesión, obtener la lista de vendedores y otros datos necesarios
            List<Usuario> listaVendedores = usuarioService.listarEmpleados();

            // Agregar los atributos al modelo
            model.addAttribute("listaVendedores", listaVendedores);
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("usuario", usuarioEnSesion);  // Agregar el usuario a enviar al formulario
            model.addAttribute("paises", paises);

            return "registro_servicio.html";  // Retornar la vista del formulario
        } else {
            // Si el usuario no está en sesión, redirigir a la página de inicio de sesión u otra página según tu lógica
            return "redirect:/login";  // Por ejemplo, redirigir a la página de inicio de sesión
        }
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,
            @RequestParam String descripcion_breve,
            @RequestParam String destino_servicio,
            @RequestParam String pais_destino,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date fecha_servicio,
            @RequestParam Double costo_servicio,
            @RequestParam TipoServicio tipoServicio,
            @RequestParam(required = false) Long id_usuario,
            @RequestParam String url_imagen1,
            @RequestParam(required = false) String url_imagen2,
            @RequestParam(required = false) String url_imagen3,
            @RequestParam(required = false) String url_imagen4,
            RedirectAttributes redirectAttrs) {

        System.out.println("id-usuario " + id_usuario);

        List<String> urls_imagenes = new ArrayList<>();
        urls_imagenes.add(url_imagen1);
        urls_imagenes.add(url_imagen2);
        urls_imagenes.add(url_imagen3);
        urls_imagenes.add(url_imagen4);

        try {
            servicioService.crearServicio(nombre, descripcion_breve,
                    destino_servicio, pais_destino,
                    fecha_servicio, costo_servicio,
                    tipoServicio, id_usuario, urls_imagenes);
            redirectAttrs.addFlashAttribute("exito", "El Servicio fue guardado con éxito");
        } catch (MyException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/";

    }

    @GetMapping("/listar/{id_usuario}")
    public String listarServicios(ModelMap model,
            @PathVariable Long id_usuario,
            @RequestParam(value = "tipoServicio", required = false) TipoServicio tipoServicio,
            @RequestParam(value = "pais_destino", required = false) String pais_destino,
            @RequestParam(value = "ordenVisitas", required = false) String ordenVisitas,
            @RequestParam(value = "ordenCantidad", required = false) String ordenCantidad, HttpSession session) {
        Usuario usuario = usuarioService.buscarUsuario(id_usuario);

        List<String> paises = new ArrayList();
        paises.addAll(List.of(
                "Afganistán", "Albania", "Alemania", "Andorra", "Angola", "Antigua y Barbuda", "Arabia Saudita",
                "Argelia", "Argentina", "Armenia", "Australia", "Austria", "Azerbaiyán", "Bahamas", "Bangladés",
                "Barbados", "Baréin", "Bélgica", "Belice", "Benín", "Bielorrusia", "Birmania", "Bolivia",
                "Bosnia y Herzegovina", "Botsuana", "Brasil", "Brunéi", "Bulgaria", "Burkina Faso", "Burundi",
                "Bután", "Cabo Verde", "Camboya", "Camerún", "Canadá", "Catar", "Chad", "Chile", "China", "Chipre",
                "Ciudad del Vaticano", "Colombia", "Comoras", "Corea del Norte", "Corea del Sur", "Costa de Marfil",
                "Costa Rica", "Croacia", "Cuba", "Dinamarca", "Dominica", "Ecuador", "Egipto", "El Salvador",
                "Emiratos Árabes Unidos", "Eritrea", "Eslovaquia", "Eslovenia", "España", "Estados Unidos", "Estonia",
                "Etiopía", "Filipinas", "Finlandia", "Fiyi", "Francia", "Gabón", "Gambia", "Georgia", "Ghana", "Granada",
                "Grecia", "Guatemala", "Guyana", "Guinea", "Guinea Ecuatorial", "Guinea-Bisáu", "Haití", "Honduras",
                "Hungría", "India", "Indonesia", "Irak", "Irán", "Irlanda", "Islandia", "Islas Marshall", "Islas Salomón",
                "Israel", "Italia", "Jamaica", "Japón", "Jordania", "Kazajistán", "Kenia", "Kirguistán", "Kiribati",
                "Kuwait", "Laos", "Lesoto", "Letonia", "Líbano", "Liberia", "Libia", "Liechtenstein", "Lituania",
                "Luxemburgo", "Macedonia del Norte", "Madagascar", "Malasia", "Malaui", "Maldivas", "Malí", "Malta",
                "Marruecos", "Mauricio", "Mauritania", "México", "Micronesia", "Moldavia", "Mónaco", "Mongolia",
                "Montenegro", "Mozambique", "Namibia", "Nauru", "Nepal", "Nicaragua", "Níger", "Nigeria", "Noruega",
                "Nueva Zelanda", "Omán", "Países Bajos", "Pakistán", "Palaos", "Panamá", "Papúa Nueva Guinea",
                "Paraguay", "Perú", "Polonia", "Portugal", "Reino Unido", "República Centroafricana", "República Checa",
                "República del Congo", "República Democrática del Congo", "República Dominicana", "Ruanda", "Rumanía",
                "Rusia", "Samoa", "San Cristóbal y Nieves", "San Marino", "San Vicente y las Granadinas", "Santa Lucía",
                "Santo Tomé y Príncipe", "Senegal", "Serbia", "Seychelles", "Sierra Leona", "Singapur", "Siria",
                "Somalia", "Sri Lanka", "Suazilandia", "Sudáfrica", "Sudán", "Sudán del Sur", "Suecia", "Suiza",
                "Surinam", "Tailandia", "Tanzania", "Tayikistán", "Timor Oriental", "Togo", "Tonga", "Trinidad y Tobago",
                "Túnez", "Turkmenistán", "Turquía", "Tuvalu", "Ucrania", "Uganda", "Uruguay", "Uzbekistán", "Vanuatu",
                "Venezuela", "Vietnam", "Yemen", "Yibuti", "Zambia", "Zimbabue"
        ));

        List<Servicio> listaServicios = new ArrayList();

        System.out.println("********************tipoServicio "+tipoServicio);
        System.out.println("pais_destino "+pais_destino);
        System.out.println("ordenVisitas "+ordenVisitas);
        System.out.println("ordenCantidad "+ordenCantidad);
        
        if (usuario != null) {
            // Verificar el rol del usuario
            if (usuario.getRol().equals(Rol.VENDEDOR)) {
                // Si el usuario es vendedor, obtener los servicios vinculados a ese vendedor
                listaServicios = servicioService.listarServiciosPorVendedor(usuario);
                model.addAttribute("listaServicios", listaServicios);
            } else {
                // Si el usuario no es vendedor, mostrar todos los servicios
                listaServicios = servicioService.listarServicios();
                model.addAttribute("listaServicios", listaServicios);
            }

            System.out.println("lista servicios cantidad " + listaServicios.size());

            List<Servicio> serviciosFiltrados = listaServicios;
            if ((tipoServicio != null) && (pais_destino != null && !pais_destino.isEmpty()) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por tipo de servicio, país y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarTipoPaisVisitas(tipoServicio, pais_destino, ordenVisitas);
            } else if ((tipoServicio != null) && (pais_destino != null && !pais_destino.isEmpty()) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por tipo de servicio, país y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarTipoPaisCantidad(tipoServicio, pais_destino, ordenCantidad);
            } else if ((pais_destino != null && !pais_destino.isEmpty()) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por país y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarPaisVisitas(pais_destino, ordenVisitas);
            } else if ((pais_destino != null && !pais_destino.isEmpty()) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por país y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarPaisCantidad(pais_destino, ordenCantidad);
            } else if ((tipoServicio != null) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por tipo de servicio y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarTipoVisitas(tipoServicio, ordenVisitas);
            } else if ((tipoServicio != null) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por tipo de servicio y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarTipoCantidad(tipoServicio, ordenCantidad);
            } else if (tipoServicio != null) {
                // búsqueda por tipo de servicio
                serviciosFiltrados = servicioService.listarPorTipoServicio(tipoServicio);
            } else if (pais_destino != null && !pais_destino.isEmpty()) {
                // búsqueda por país
                serviciosFiltrados = servicioService.listarPorPais(pais_destino);
            } else if (ordenCantidad != null && !ordenCantidad.isEmpty()) {
                // ordena por cantidad de ventas
                serviciosFiltrados = servicioService.listarMasVendidos();
            } else if (ordenVisitas != null && !ordenVisitas.isEmpty()) {
                // ordena por cantidad de visitas
                serviciosFiltrados = servicioService.listarMasVisitados();
            }

            // Obtener y agregar los servicios del carrito para mostrarlos en la vista
            List<Servicio> serviciosCarrito = ventaService.obtenerCarritoDesdeSesion(session);

            model.addAttribute("serviciosFiltrados", serviciosFiltrados);
            model.addAttribute("serviciosCarrito", serviciosCarrito);
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("logueado", usuario);
            model.addAttribute("paises", paises);
            model.addAttribute("servicio", new Servicio());

        }
        return "panel_servicios.html";
    }

    @GetMapping("/listarPorTipo/{codigo}")
    public String listarServiciosPorTipo(ModelMap model,
            @PathVariable int codigo,
            @RequestParam(value = "pais_destino", required = false) String pais_destino,
            @RequestParam(value = "ordenVisitas", required = false) String ordenVisitas,
            @RequestParam(value = "ordenCantidad", required = false) String ordenCantidad, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");


        System.out.println("******************* pais_destino "+pais_destino);
        System.out.println("ordenVisitas "+ordenVisitas);
        System.out.println("ordenCantidad "+ordenCantidad);
        List<String> paises = new ArrayList();
        paises.addAll(List.of(
                "Afganistán", "Albania", "Alemania", "Andorra", "Angola", "Antigua y Barbuda", "Arabia Saudita",
                "Argelia", "Argentina", "Armenia", "Australia", "Austria", "Azerbaiyán", "Bahamas", "Bangladés",
                "Barbados", "Baréin", "Bélgica", "Belice", "Benín", "Bielorrusia", "Birmania", "Bolivia",
                "Bosnia y Herzegovina", "Botsuana", "Brasil", "Brunéi", "Bulgaria", "Burkina Faso", "Burundi",
                "Bután", "Cabo Verde", "Camboya", "Camerún", "Canadá", "Catar", "Chad", "Chile", "China", "Chipre",
                "Ciudad del Vaticano", "Colombia", "Comoras", "Corea del Norte", "Corea del Sur", "Costa de Marfil",
                "Costa Rica", "Croacia", "Cuba", "Dinamarca", "Dominica", "Ecuador", "Egipto", "El Salvador",
                "Emiratos Árabes Unidos", "Eritrea", "Eslovaquia", "Eslovenia", "España", "Estados Unidos", "Estonia",
                "Etiopía", "Filipinas", "Finlandia", "Fiyi", "Francia", "Gabón", "Gambia", "Georgia", "Ghana", "Granada",
                "Grecia", "Guatemala", "Guyana", "Guinea", "Guinea Ecuatorial", "Guinea-Bisáu", "Haití", "Honduras",
                "Hungría", "India", "Indonesia", "Irak", "Irán", "Irlanda", "Islandia", "Islas Marshall", "Islas Salomón",
                "Israel", "Italia", "Jamaica", "Japón", "Jordania", "Kazajistán", "Kenia", "Kirguistán", "Kiribati",
                "Kuwait", "Laos", "Lesoto", "Letonia", "Líbano", "Liberia", "Libia", "Liechtenstein", "Lituania",
                "Luxemburgo", "Macedonia del Norte", "Madagascar", "Malasia", "Malaui", "Maldivas", "Malí", "Malta",
                "Marruecos", "Mauricio", "Mauritania", "México", "Micronesia", "Moldavia", "Mónaco", "Mongolia",
                "Montenegro", "Mozambique", "Namibia", "Nauru", "Nepal", "Nicaragua", "Níger", "Nigeria", "Noruega",
                "Nueva Zelanda", "Omán", "Países Bajos", "Pakistán", "Palaos", "Panamá", "Papúa Nueva Guinea",
                "Paraguay", "Perú", "Polonia", "Portugal", "Reino Unido", "República Centroafricana", "República Checa",
                "República del Congo", "República Democrática del Congo", "República Dominicana", "Ruanda", "Rumanía",
                "Rusia", "Samoa", "San Cristóbal y Nieves", "San Marino", "San Vicente y las Granadinas", "Santa Lucía",
                "Santo Tomé y Príncipe", "Senegal", "Serbia", "Seychelles", "Sierra Leona", "Singapur", "Siria",
                "Somalia", "Sri Lanka", "Suazilandia", "Sudáfrica", "Sudán", "Sudán del Sur", "Suecia", "Suiza",
                "Surinam", "Tailandia", "Tanzania", "Tayikistán", "Timor Oriental", "Togo", "Tonga", "Trinidad y Tobago",
                "Túnez", "Turkmenistán", "Turquía", "Tuvalu", "Ucrania", "Uganda", "Uruguay", "Uzbekistán", "Vanuatu",
                "Venezuela", "Vietnam", "Yemen", "Yibuti", "Zambia", "Zimbabue"
        ));

        List<Servicio> listaServicios = new ArrayList();
        List<Servicio> listaService = new ArrayList();

        TipoServicio tipoServicio = TipoServicio.AUTO;

        switch (codigo) {
            case 1:
                tipoServicio = tipoServicio.HOTELES;
                break;
            case 2:
                tipoServicio = tipoServicio.AUTO;
                break;
            case 3:
                tipoServicio = tipoServicio.COLECTIVO;
                break;
            case 4:
                tipoServicio = tipoServicio.AVION;
                break;
            case 5:
                tipoServicio = tipoServicio.TREN;
                break;
            case 6:
                tipoServicio = tipoServicio.EXCURSIONES;
                break;
            case 7:
                tipoServicio = tipoServicio.EVENTOS;
                break;
        }

        final TipoServicio tipoServicioFiltrar = tipoServicio;

        if (usuario != null) {
            // Verificar el rol del usuario
            if (usuario.getRol().equals(Rol.VENDEDOR)) {
                // Si el usuario es vendedor, obtener los servicios vinculados a ese vendedor
                listaService = servicioService.listarServiciosPorVendedor(usuario);
                listaServicios = listaService.stream().filter(servicio -> servicio.getTipoServicio().equals(tipoServicioFiltrar)).collect(Collectors.toList());

                model.addAttribute("listaServicios", listaServicios);
            } else {
                // Si el usuario no es vendedor, mostrar todos los servicios
                listaServicios = servicioService.listarPorTipoServicio(tipoServicio);
                model.addAttribute("listaServicios", listaServicios);
            }

            List<Servicio> serviciosFiltrados = listaServicios;
            if ((tipoServicio != null) && (pais_destino != null && !pais_destino.isEmpty()) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por tipo de servicio, país y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarTipoPaisVisitas(tipoServicio, pais_destino, ordenVisitas);
            } else if ((tipoServicio != null) && (pais_destino != null && !pais_destino.isEmpty()) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por tipo de servicio, país y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarTipoPaisCantidad(tipoServicio, pais_destino, ordenCantidad);
            } else if ((pais_destino != null && !pais_destino.isEmpty()) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por país y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarPaisVisitas(pais_destino, ordenVisitas);
            } else if ((pais_destino != null && !pais_destino.isEmpty()) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por país y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarPaisCantidad(pais_destino, ordenCantidad);
            } else if ((tipoServicio != null) && (ordenVisitas != null && !ordenVisitas.isEmpty())) {
                // búsqueda por tipo de servicio y ordenada por cantidad de visitas
                serviciosFiltrados = servicioService.filtrarTipoVisitas(tipoServicio, ordenVisitas);
            } else if ((tipoServicio != null) && (ordenCantidad != null && !ordenCantidad.isEmpty())) {
                // búsqueda por tipo de servicio y ordenada por cantidad de ventas
                serviciosFiltrados = servicioService.filtrarTipoCantidad(tipoServicio, ordenCantidad);
            } else if (pais_destino != null && !pais_destino.isEmpty()) {
                // búsqueda por país
                System.out.println("***************filtro pais "+pais_destino);
                serviciosFiltrados = servicioService.listarPorPais(pais_destino);
            } else if (ordenCantidad != null && !ordenCantidad.isEmpty()) {
                // ordena por cantidad de ventas
                serviciosFiltrados = servicioService.listarMasVendidos();
            } else if (ordenVisitas != null && !ordenVisitas.isEmpty()) {
                // ordena por cantidad de visitas
                serviciosFiltrados = servicioService.listarMasVisitados();
            }

            List<Servicio> serviciosCarrito = ventaService.obtenerCarritoDesdeSesion(session);

            model.addAttribute("serviciosFiltrados", serviciosFiltrados);
            model.addAttribute("serviciosCarrito", serviciosCarrito);
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("logueado", usuario);
            model.addAttribute("paises", paises);
            model.addAttribute("servicio", new Servicio());
            model.addAttribute("codigo", codigo);

        }
        return "ver_mas_servicios.html";
    }

    @GetMapping("/modificar/{codigo_servicio}")
    public String modificar(@PathVariable Long codigo_servicio, Model model) {

        Servicio servicio = servicioService.obtenerPorId(codigo_servicio);
        List<Usuario> listaVendedores = usuarioService.listarEmpleados();

        System.out.println("lista vendedores " + listaVendedores);

        model.addAttribute("listaVendedores", listaVendedores);
        model.addAttribute("tipos", TipoServicio.values());

        model.addAttribute("servicio", servicio);
        model.addAttribute("imagenes", servicio.getImagenes()); // Agregar la lista de imágenes al modelo

        return "modificar-servicio.html";
    }

    @PostMapping("/modificar")
    public String modificar(
            @RequestParam Long codigo_servicio,
            @RequestParam String nombre,
            @RequestParam String descripcion_breve,
            @RequestParam String destino_servicio,
            @RequestParam String pais_destino,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_servicio,
            @RequestParam Double costo_servicio,
            @RequestParam TipoServicio tipoServicio,
            @RequestParam(required = false) Long id_usuario,
            @RequestParam(required = false) String id_imagenes, // Cambio en la firma para recibir una cadena de IDs
            @RequestParam(required = false) String url_imagen1,
            @RequestParam(required = false) String url_imagen2,
            @RequestParam(required = false) String url_imagen3,
            @RequestParam(required = false) String url_imagen4,
            RedirectAttributes redirectAttrs) {

        System.out.println("id-usuario " + id_usuario);

        List<String> urls_imagenes = new ArrayList<>();
        urls_imagenes.add(url_imagen1);
        urls_imagenes.add(url_imagen2);
        urls_imagenes.add(url_imagen3);
        urls_imagenes.add(url_imagen4);

        try {
            // Agregar logs para verificar los datos recibidos
            System.out.println("ID del servicio: " + codigo_servicio);
            System.out.println("Nombre: " + nombre);
            System.out.println("Descripción breve: " + descripcion_breve);
            System.out.println("Destino: " + destino_servicio);
            System.out.println("País destino: " + pais_destino);
            System.out.println("Fecha servicio: " + fecha_servicio);
            System.out.println("Costo servicio: " + costo_servicio);
            System.out.println("Tipo servicio: " + tipoServicio);
            System.out.println("ID del usuario: " + id_usuario);
            System.out.println("IDs de imágenes existentes: " + id_imagenes);
            System.out.println("URLs de imágenes nuevas: " + urls_imagenes);

            if (id_imagenes != null && !id_imagenes.isEmpty()) {
                List<Long> idList = Arrays.stream(id_imagenes.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                // Lógica para crear el servicio utilizando los parámetros recibidos
                servicioService.modificarServicio(codigo_servicio, nombre, descripcion_breve, destino_servicio, pais_destino,
                        fecha_servicio, costo_servicio, tipoServicio, id_usuario, idList, urls_imagenes); // Modificación en la llamada al servicio
                redirectAttrs.addFlashAttribute("exito", "El Servicio fue guardado con éxito");
            } else {
                // Manejar el caso cuando id_imagenes es null o vacío
                redirectAttrs.addFlashAttribute("error", "No se proporcionaron IDs de imágenes válidos.");
            }

        } catch (MyException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/"; // Redirige a la página principal después de procesar el formulario
    }

    @GetMapping("/detalle/{codigo_servicio}")
    public String detalle(@PathVariable Long codigo_servicio, Model model) {

        Servicio servicio = servicioService.obtenerPorId(codigo_servicio);

        List<Imagen> imagenes = servicio.getImagenes();

        imagenes.forEach(imagen -> System.out.println("url imagen: " + imagen.getUrl()));

        servicioService.actualizarVisitasServicio(servicio);

        model.addAttribute("servicio", servicio);
        model.addAttribute("imagenes", imagenes);

        return "detalle_servicio.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
        
        System.out.println("codigo servicio "+ id);
        try {
            servicioService.eliminarPorId(id);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        System.out.println("logueado " + logueado);
        Long userId = logueado.getId_usuario();

        System.out.println("userId " + userId);
        if (userId == null) {
            // Manejar caso de sesión inválida
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesion");
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        }
         redirectAttrs.addFlashAttribute("exito", "Servicio eliminado con éxito");
        // Redirigir al panel de servicios con el ID del usuario
        return "redirect:/servicio/listar/" + userId;
           
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
               return "redirect:/"; 
        }
        
    }

}
