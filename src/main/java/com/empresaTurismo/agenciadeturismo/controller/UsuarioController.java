package com.empresaTurismo.agenciadeturismo.controller;

import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.entidades.Venta;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.servicio.ServicioService;
import com.empresaTurismo.agenciadeturismo.servicio.UsuarioService;
import com.empresaTurismo.agenciadeturismo.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {

        model.addAttribute("roles", Rol.values());

        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String direccion,
            @RequestParam String dni,
            @RequestParam String fecha_nac,
            @RequestParam String nacionalidad,
            @RequestParam Long celular,
            @RequestParam String email,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Double sueldo,
            @RequestParam(required = false) Rol rol,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo, HttpSession session,
            RedirectAttributes redirectAttrs) {

        try {
            // Validar el formato del número de documento
            if (!dni.matches("\\d{1,8}")) {
                throw new IllegalArgumentException("El número de documento no es válido");
            }

            // Convertir la fecha de String a Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNacimiento = sdf.parse(fecha_nac);

            System.out.println(" fecha nac " + fechaNacimiento);
            // Calcular la edad del usuario
            Calendar fechaNacimientoCal = Calendar.getInstance();
            fechaNacimientoCal.setTime(fechaNacimiento);
            Calendar hoy = Calendar.getInstance();

            int edad = hoy.get(Calendar.YEAR) - fechaNacimientoCal.get(Calendar.YEAR);

            // Verificar si ya ha pasado el cumpleaños en el año actual
            Calendar cumpleañosEsteAño = Calendar.getInstance();
            cumpleañosEsteAño.setTime(fechaNacimiento);
            cumpleañosEsteAño.set(Calendar.YEAR, hoy.get(Calendar.YEAR));
            if (cumpleañosEsteAño.after(hoy)) {
                edad--;  // Todavía no ha cumplido años este año
            }

            System.out.println("fecha nac " + fechaNacimientoCal.get(Calendar.YEAR));
            System.out.println("hoy " + hoy.get(Calendar.YEAR));
            System.out.println("edad " + edad);

            // Validar la edad del usuario
            if (edad >= 18 && edad < 123) {
                usuService.crearUsuario(nombre, apellido, direccion, dni,
                        fechaNacimiento, nacionalidad, celular,
                        email, cargo, sueldo, rol, password, password2);
                redirectAttrs.addFlashAttribute("exito", "El usuario se registró correctamente");
                return "redirect:/"; // Página de éxito
            } else {
                redirectAttrs.addFlashAttribute("error", "Debe ser mayor de 18 años para registrarse.");
                return "registro.html";
            }

        } catch (ParseException | IllegalArgumentException ex) {

            System.out.println("Error al procesar el registro: " + ex.getMessage());
            redirectAttrs.addFlashAttribute("error", "Error al procesar el registro: ");
            return "registro.html"; // Página de error

        } catch (MyException ex) {

            System.out.println("El usuario no pudo ser registrado " + ex);
            redirectAttrs.addFlashAttribute("error", "El usuario no pudo ser registrado");
            return "registro.html"; // Página de error
        }
    }

    @GetMapping("/modificar/{id}")
    public String panelPerfil(@PathVariable Long id, Model model) {

        Usuario usuario = usuService.buscarUsuario(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());

        return "usuario_modificar.html";
    }

    @PostMapping("/modificar")
    public String modificarUsuario(
            @RequestParam Long id_usuario,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String direccion,
            @RequestParam String dni,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_nac,
            @RequestParam String nacionalidad,
            @RequestParam Long celular,
            @RequestParam String email,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Double sueldo,
            @RequestParam(required = false) Rol rol,
            @RequestParam String password,
            @RequestParam(required = false) Boolean estado,
            ModelMap modelo, HttpSession session,
            RedirectAttributes redirectAttrs) {

        try {
            usuService.modificarUsuario(id_usuario, nombre, apellido, direccion, dni, fecha_nac,
                    nacionalidad, celular, email, cargo, sueldo, rol, password, estado);
            redirectAttrs.addFlashAttribute("exito", "El usuario se modificó correctamente");
            return "redirect:/"; // Redirige a la página de inicio o a donde necesites
        } catch (MyException ex) {
            redirectAttrs.addFlashAttribute("error", "Error al procesar la modificación: " + ex.getMessage());
            return "redirect:/usuario/modificar"; // Página de error
        }
    }

    @GetMapping("/listar")
    public String lista(Model model) {

        //agrego la lista para que cargue los usuarios
        List<Usuario> usuarios = usuService.listaUsuarios();
        List<Usuario> vendedores = usuarios.stream()
                .filter(u -> u.getRol() == Rol.VENDEDOR)
                .collect(Collectors.toList());

        List<Usuario> clientes = usuarios.stream()
                .filter(u -> u.getRol() == Rol.CLIENTE)
                .collect(Collectors.toList());

        List<Usuario> administradores = usuarios.stream()
                .filter(u -> u.getRol() == Rol.ADMIN)
                .collect(Collectors.toList());

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("clientes", clientes);
        model.addAttribute("administradores", administradores);

        return "panel_usuarios.html";

    }

    @GetMapping("/reporteVentas/{id}")
    public String reporteVentas(@PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = usuService.buscarUsuario(id);
        List<Venta> listaVentas = ventaService.listarVentas();

        List<Venta> ventasTabla = new ArrayList<>();
        Double comisionTotal = 0.0;

        if (usuario != null) {
            for (Venta venta : listaVentas) {
                // Crear un objeto VentaTabla para representar cada venta en la tabla
                Venta ventaTabla = new Venta();

                // Obtener la lista de usuarios asociados a la venta
                List<Usuario> listaUsuariosVenta = venta.getUsuario();

                // Crear una lista de vendedores para esta venta
                List<Usuario> vendedores = new ArrayList<>();

                System.out.println("*************** listaUsuariosVenta " + listaUsuariosVenta);
                // Iterar sobre la lista de usuarios para encontrar al vendedor
                for (Usuario usuarioVenta : listaUsuariosVenta) {
                    if (usuarioVenta.getRol().equals(Rol.VENDEDOR)) {
                        vendedores.add(usuarioVenta);
                    }
                }
                // Asignar los demás atributos de la ventaTabla
                ventaTabla.setUsuario(vendedores);
                ventaTabla.setFecha_venta(venta.getFecha_venta());
                ventaTabla.setMedio_pago(venta.getMedio_pago());
                ventaTabla.setPrecioTotalVenta(venta.getPrecioTotalVenta());

                // Calcular la comisión según el medio de pago
                double comisionVenta = 0.0;
                switch (venta.getMedio_pago()) {
                    case "Tarjeta":
                        comisionVenta = venta.getPrecioTotalVenta() * 0.09;
                        break;
                    case "TarjetaDebito":
                        comisionVenta = venta.getPrecioTotalVenta() * 0.03;
                        break;
                    case "Transferencia":
                        comisionVenta = venta.getPrecioTotalVenta() * 0.0245;
                        break;
                    default:
                        break;
                }
                ventaTabla.setComision(comisionVenta);
                comisionTotal += comisionVenta;

                // Agregar la ventaTabla a la lista de ventasTabla
                ventasTabla.add(ventaTabla);
            }
        }
        Usuario mejorVendedor = ventaService.obtenerMejorVendedor(listaVentas);

        // Agregar las listas y valores necesarios al modelo
        model.addAttribute("ventasTabla", ventasTabla);
        model.addAttribute("mejorVendedor", mejorVendedor);
        model.addAttribute("comisionTotal", comisionTotal);

        return "reporte-ventas";
    }

    @PostMapping("/reporte/{id}")
    public String reporte(@PathVariable Long id,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String medioPago,
            Model model,
            RedirectAttributes redirectAttrs) {

        List<Venta> listaVentas = ventaService.listarVentas();
        listaVentas = ventaService.filtrarVentasPorRolVendedor(listaVentas);

        List<Venta> listaVentaFiltradaPorFecha = ventaService.buscarPorFecha(fechaInicio, fechaFin);
        List<Venta> listaFiltrada = filtrarVentas(listaVentaFiltradaPorFecha, medioPago);

        Usuario mejorVendedor = ventaService.obtenerMejorVendedor(listaFiltrada);

        List<Map<String, Object>> reporteDiarioList = generarReporteDiario(listaFiltrada);
        List<Map<String, Object>> reporteMensualList = generarReporteMensual(listaFiltrada);
        List<Map<String, Object>> reporteAnualList = generarReporteAnual(listaFiltrada);

        model.addAttribute("ventasTabla", listaVentas);
        model.addAttribute("mejorVendedor", mejorVendedor);
        model.addAttribute("reporteDiarioList", reporteDiarioList);
        model.addAttribute("reporteMensualList", reporteMensualList);
        model.addAttribute("reporteAnualList", reporteAnualList);

        return "reporte-ventas";
    }

    private List<Map<String, Object>> generarReporteAnual(List<Venta> ventas) {
        Map<Integer, List<Venta>> ventasPorAnio = agruparVentasPorAnio(ventas);
        return generarReporteAnual(ventasPorAnio);
    }

    private Map<Integer, List<Venta>> agruparVentasPorAnio(List<Venta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(
                        venta -> {
                            Date fechaVentaUtil = new Date(venta.getFecha_venta().getTime());
                            LocalDate localDate = fechaVentaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return localDate.getYear();
                        },
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private List<Map<String, Object>> generarReporteAnual(Map<Integer, List<Venta>> ventasPorAnio) {
        List<Map<String, Object>> reporteAnualList = new ArrayList<>();
        for (Map.Entry<Integer, List<Venta>> entry : ventasPorAnio.entrySet()) {
            int anio = entry.getKey();
            List<Venta> ventasDelAnio = entry.getValue();

            // Calcular cantidades para este año
            int cantidadVentas = ventasDelAnio.size();
            double costoTotalVentas = ventasDelAnio.stream().mapToDouble(Venta::getPrecioTotalVenta).sum();
            double comisionesAPagar = ventasDelAnio.stream().mapToDouble(Venta::getComision).sum();
            double gananciasAnuales = costoTotalVentas - comisionesAPagar;

            // Crear un mapa para representar los datos del reporte anual
            Map<String, Object> reporteAnual = new HashMap<>();
            reporteAnual.put("anio", anio);
            reporteAnual.put("cantidadVentas", cantidadVentas);
            reporteAnual.put("costoTotalVentas", costoTotalVentas);
            reporteAnual.put("comisionesAPagar", comisionesAPagar);
            reporteAnual.put("gananciasAnuales", gananciasAnuales);

            // Agregar el mapa a la lista de reporteAnualList
            reporteAnualList.add(reporteAnual);
        }
        return reporteAnualList;
    }

    private List<Venta> filtrarVentas(List<Venta> ventas, String medioPago) {
        if (medioPago == null || medioPago.equalsIgnoreCase("todos")) {
            return ventas;
        } else {
            return ventas.stream()
                    .filter(venta -> venta.getMedio_pago().equalsIgnoreCase(medioPago))
                    .collect(Collectors.toList());
        }
    }

    private List<Map<String, Object>> generarReporteDiario(List<Venta> ventas) {
        Map<LocalDate, List<Venta>> ventasPorFecha = agruparVentasPorFecha(ventas);

        List<Map<String, Object>> reporteDiarioList = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Venta>> entry : ventasPorFecha.entrySet()) {
            LocalDate fechaVenta = entry.getKey();
            List<Venta> ventasDelDia = entry.getValue();

            int cantidadVentas = ventasDelDia.size();
            double costoTotalVentas = ventasDelDia.stream().mapToDouble(Venta::getPrecioTotalVenta).sum();
            double comisionesAPagar = ventasDelDia.stream().mapToDouble(Venta::getComision).sum();
            double gananciasDiarias = costoTotalVentas - comisionesAPagar;

            Map<String, Object> reporteDiario = new HashMap<>();
            reporteDiario.put("fechaVenta", fechaVenta);
            reporteDiario.put("cantidadVentas", cantidadVentas);
            reporteDiario.put("costoTotalVentas", costoTotalVentas);
            reporteDiario.put("comisionesAPagar", comisionesAPagar);
            reporteDiario.put("gananciasDiarias", gananciasDiarias);

            reporteDiarioList.add(reporteDiario);
        }
        return reporteDiarioList;
    }

    private Map<LocalDate, List<Venta>> agruparVentasPorFecha(List<Venta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(
                        venta -> {
                            // Convertir java.sql.Date a java.util.Date
                            Date fechaVentaUtil = new Date(venta.getFecha_venta().getTime());
                            return fechaVentaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        },
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private List<Map<String, Object>> generarReporteMensual(List<Venta> ventas) {
        Map<YearMonth, List<Venta>> ventasPorMes = agruparVentasPorMes(ventas);

        List<Map<String, Object>> reporteMensualList = new ArrayList<>();
        for (Map.Entry<YearMonth, List<Venta>> entry : ventasPorMes.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            List<Venta> ventasDelMes = entry.getValue();

            int cantidadVentas = ventasDelMes.size();
            double costoTotalVentas = ventasDelMes.stream().mapToDouble(Venta::getPrecioTotalVenta).sum();
            double comisionesAPagar = ventasDelMes.stream().mapToDouble(Venta::getComision).sum();
            double gananciasMensuales = costoTotalVentas - comisionesAPagar;

            Map<String, Object> reporteMensual = new HashMap<>();
            reporteMensual.put("mes", yearMonth.getMonth().toString());
            reporteMensual.put("cantidadVentas", cantidadVentas);
            reporteMensual.put("costoTotalVentas", costoTotalVentas);
            reporteMensual.put("comisionesAPagar", comisionesAPagar);
            reporteMensual.put("gananciasMensuales", gananciasMensuales);

            reporteMensualList.add(reporteMensual);
        }
        return reporteMensualList;
    }

    private Map<YearMonth, List<Venta>> agruparVentasPorMes(List<Venta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(
                        venta -> {
                            // Convertir java.sql.Date a java.util.Date
                            Date fechaVentaUtil = new Date(venta.getFecha_venta().getTime());
                            LocalDate localDate = fechaVentaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return YearMonth.from(localDate);
                        },
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

}
