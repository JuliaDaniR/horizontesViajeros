package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.entidades.Paquete;
import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.entidades.Venta;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.enumerador.TipoServicio;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.servicio.PaqueteService;
import com.empresaTurismo.agenciadeturismo.servicio.ServicioService;
import com.empresaTurismo.agenciadeturismo.servicio.UsuarioService;
import com.empresaTurismo.agenciadeturismo.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioService usuService;

    @Autowired
    private PaqueteService paqueteService;

    @GetMapping("/carrito/{id}")
    public String carritoCompra(@PathVariable Long id, ModelMap model, HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        System.out.println("logueado " + logueado);

        Servicio servicio = servicioService.obtenerPorId(id);
        List<Servicio> carrito = ventaService.obtenerCarritoDesdeSesion(session);

        // Calcular subtotal y total
        double subtotal = carrito.stream().mapToDouble(item -> item.getCantidad() * item.getCosto_servicio()).sum();
        double total = subtotal;

        System.out.println(" **********/carrito/{id}**********subtotal " + subtotal + "** total **" + total + "************************");
        // Aplicar descuento del 10% si hay más de un servicio en el carrito
        if (carrito.size() > 1) {
            double descuento = subtotal * 0.10; // 10% de descuento
            total -= descuento;
        }

        String detalleProducto = "";

        TipoServicio tipoServicio = servicio.getTipoServicio();

        switch (tipoServicio) {
            case HOTELES:
                detalleProducto = "El precio es por día";
                break;
            case AUTO:
                detalleProducto = "El precio es por 12hs";
                break;
            default:
                detalleProducto = "El precio es por unidad";
                break;
        }

        model.addAttribute("detalleProducto", detalleProducto);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", subtotal - total); // Mostrar el descuento aplicado
        model.addAttribute("total", total);
        model.addAttribute("servicio", servicio);
        model.addAttribute("logueado", logueado);
        model.addAttribute("contador", 1);

        return "carrito.html";
    }

    @PostMapping("/agregarAlCarrito")
    public String agregarAlCarrito(
            @RequestParam Long servicioId,
            @RequestParam String cantidadStr,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            int cantidad = Integer.parseInt(cantidadStr);

            Servicio servicio = servicioService.obtenerPorId(servicioId);

            List<Servicio> carrito = obtenerCarritoDesdeSesion(session);
            boolean encontrado = false;

            for (Servicio item : carrito) {
                if (item.getCodigo_servicio().equals(servicio.getCodigo_servicio())) {
                    // Si el servicio ya está en el carrito, actualizar la cantidad
                    item.setCantidad(item.getCantidad() + cantidad);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                // Si el servicio no estaba en el carrito, agregarlo con la cantidad especificada
                servicio.setCantidad(cantidad);
                carrito.add(servicio);
            }

            // Actualizar el carrito en la sesión
            session.setAttribute("carrito", carrito);

            // Calcular el subtotal actualizado
            double subtotal = carrito.stream()
                    .mapToDouble(item -> item.getCantidad() * item.getCosto_servicio())
                    .sum();

            // Actualizar el subtotal en la sesión
            session.setAttribute("subtotal", subtotal);

            // Obtener el ID de usuario de la sesión para la redirección
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            Long userId = logueado.getId_usuario();
            if (userId == null) {

                redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para agregar al carrito.");
                return "redirect:/login";
            }

            return "redirect:/servicio/listar/" + userId;
        } catch (NumberFormatException e) {

            redirectAttributes.addFlashAttribute("error", "Error al agregar al carrito.");
            return "redirect:/";
        }
    }

    @PostMapping("/eliminarDelCarrito")
    public String eliminarDelCarrito(@RequestParam Long servicioId, HttpSession session, RedirectAttributes redirectAttributes) {
        ventaService.eliminarDelCarrito(servicioId, session);

        // Obtener el ID de usuario de la sesión para la redirección
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        System.out.println("logueado " + logueado);
        Long userId = logueado.getId_usuario();

        System.out.println("userId " + userId);
        if (userId == null) {
            // Manejar caso de sesión inválida
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        }
        // Redirigir al panel de servicios con el ID del usuario
        return "redirect:/servicio/listar/" + userId;
    }

    @GetMapping("/carritoDetalle")
    public String carritoCompra(ModelMap model, HttpSession session) throws MyException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        // Obtener el carrito desde la sesión
        List<Servicio> carrito = obtenerCarritoDesdeSesion(session);
        List<Long> id_servicios = new ArrayList<>();
        Servicio service = new Servicio();
        List<Usuario> usuarios = new ArrayList<>();
        Paquete paquete = new Paquete();

        usuarios.add(logueado);

        // Calcular subtotal y total
        double subtotal = carrito.stream().mapToDouble(servicio -> servicio.getCantidad() * servicio.getCosto_servicio()).sum();
        double total = subtotal;

        System.out.println(" *********/detalleCarrito***********subtotal " + subtotal + "** total **" + total + "************************");

        // Aplicar descuento del 10% si hay más de un servicio en el carrito
        if (carrito.size() > 1) {
            double descuento = subtotal * 0.10; // 10% de descuento
            total -= descuento;
            for (Servicio servicio : carrito) {
                id_servicios.add(servicio.getCodigo_servicio());
                usuarios.add(servicio.getVendedor());
            }
            paquete = paqueteService.crearPaquete(id_servicios, total);
        } else {
            for (Servicio servicio : carrito) {
                service = servicioService.obtenerPorId(servicio.getCodigo_servicio());
                usuarios.add(servicio.getVendedor());
            }
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicio", service);
        model.addAttribute("paqueteTuristico", paquete);
        model.addAttribute("carrito", carrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", subtotal - total); // Mostrar el descuento aplicado
        model.addAttribute("total", total);

        return "detalleCarrito.html";
    }

// Método para obtener el carrito desde la sesión
    private List<Servicio> obtenerCarritoDesdeSesion(HttpSession session) {

        // Implementación para obtener el carrito desde la sesión
        List<Servicio> carrito = (List<Servicio>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {

        // Aquí puedes agregar lógica para obtener datos necesarios del sistema
        List<Usuario> usuarios = usuService.listaUsuarios();
        List<Usuario> vendedores = usuarios.stream()
                .filter(u -> u.getRol() == Rol.VENDEDOR)
                .collect(Collectors.toList());

        List<Usuario> clientes = usuarios.stream()
                .filter(u -> u.getRol() == Rol.CLIENTE)
                .collect(Collectors.toList());
        List<Servicio> servicios = servicioService.listarServicios(); // Suponiendo que tienes un servicio para obtener servicios
        List<Paquete> paquetes = paqueteService.listarPaquetes(); // Suponiendo que tienes un servicio para obtener paquetes

        Venta venta = new Venta();

        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clientes);
        model.addAttribute("empleados", vendedores);
        model.addAttribute("servicios", servicios);
        model.addAttribute("paquetes", paquetes);

        return "registro_venta.html";
    }

    @PostMapping("/registrarVenta")
    public String registro(
            @RequestParam(required = false) Date fecha_venta,
            @RequestParam String medioPago,
            @RequestParam List<Long> usuarioIds,
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long paqueteId,
            HttpSession session,
            RedirectAttributes redirectAttrs,
            ModelMap model) {

        // Obtener el carrito desde la sesión
        List<Servicio> carrito = obtenerCarritoDesdeSesion(session);
        List<Usuario> usuarios = new ArrayList<>();

        for (Long usuarioId : usuarioIds) {
            Usuario usu = usuService.buscarUsuario(usuarioId);
            usuarios.add(usu);
        }

        // Verificar si hay elementos en el carrito
        if (carrito.isEmpty()) {
            // Manejar caso de carrito vacío, redirigir al carrito
            return "redirect:/carritoDetalle";
        }

        // Calcular subtotal y total
        double subtotal = carrito.stream().mapToDouble(servicio -> servicio.getCantidad() * servicio.getCosto_servicio()).sum();
        double total = subtotal;

        // Aplicar descuento del 10% si hay más de un servicio en el carrito
        if (carrito.size() > 1) {
            double descuento = subtotal * 0.10; // 10% de descuento
            total -= descuento;
        }

        for (Servicio serv : carrito) {
            Servicio servicio = servicioService.obtenerPorId(serv.getCodigo_servicio());
            servicio.setCantidad(servicio.getCantidad() + serv.getCantidad());
            servicioService.actualizarCantidadServicio(servicio);
            System.out.println("cantidad serv carrito" + serv.getCantidad());
            System.out.println("cantidad servicio " + servicio.getCantidad());
        }

        // Determinar el tipo de compra según la cantidad de elementos en el carrito
        String tipoCompra = carrito.size() > 1 ? "paquete" : "servicio";

        // Si la fecha de venta es nula, establecer la fecha actual
        if (fecha_venta == null) {
            fecha_venta = new Date();
        }

        System.out.println(" total " + total);
        try {
            // Llamar al servicio de venta para crear la venta
            if ("servicio".equalsIgnoreCase(tipoCompra)) {
                // Crear venta de servicio individual
                ventaService.crearVenta(fecha_venta, medioPago, total, usuarios, servicioId, null);
            } else if ("paquete".equalsIgnoreCase(tipoCompra)) {
                // Crear venta de paquete turístico
                ventaService.crearVenta(fecha_venta, medioPago, total, usuarios, null, paqueteId);
            } else {
                // Tipo de compra no reconocido, manejar error
                throw new MyException("Tipo de compra no válido");
            }

            // Limpiar el carrito después de realizar la compra
            session.removeAttribute("carrito");

            redirectAttrs.addFlashAttribute("exito", "La compra se registró correctamente");

            // Redirigir a una página de confirmación o detalle de la compra
            return "redirect:/";

        } catch (MyException ex) {

            redirectAttrs.addFlashAttribute("error", "La compra no se registró correctamente. Intente nuevamente");

            return "redirect:/"; // Redirigir a página de error
        }
    }

}
