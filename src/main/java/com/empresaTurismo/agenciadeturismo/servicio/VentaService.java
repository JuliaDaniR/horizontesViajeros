package com.empresaTurismo.agenciadeturismo.servicio;

import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.entidades.Paquete;
import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Venta;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.repositorio.IPaqueteRepository;
import com.empresaTurismo.agenciadeturismo.repositorio.IServicioRepository;
import com.empresaTurismo.agenciadeturismo.repositorio.IVentaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.empresaTurismo.agenciadeturismo.repositorio.IUsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class VentaService {

    @Autowired
    private IVentaRepository ventaRepo;

    @Autowired
    private IUsuarioRepository usuarioRepo;

    @Autowired
    private IServicioRepository servicioRepo;

    @Autowired
    private IPaqueteRepository paqueteRepo;

    @Transactional
    public void crearVenta(Date fecha_venta, String medio_pago, Double total,
            List<Usuario> usuarios, Long id_servicio,
            Long id_paqueteTuristico) throws MyException {
        validarDatos(fecha_venta, medio_pago, usuarios);

        Venta venta = new Venta();

        if (id_servicio != null) {
            // Es una venta de servicio individual
            Servicio servicio = servicioRepo.findById(id_servicio)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró ningún servicio con el ID proporcionado"));
            venta.setServicio(servicio);
        } else if (id_paqueteTuristico != null) {
            // Es una venta de paquete turístico
            Paquete paquete = paqueteRepo.findById(id_paqueteTuristico)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró ningún paquete con el ID proporcionado"));
            venta.setPaqueteTuristico(paquete);
        } else {
            throw new IllegalArgumentException("Se debe proporcionar un servicio o un paquete para realizar la venta");
        }

        double comisionVenta = 0.0;

        switch (medio_pago) {
            case "Tarjeta":
                comisionVenta = total * 0.09;
                break;
            case "TarjetaDebito":
                comisionVenta = total * 0.03;
                break;
            case "Transferencia":
                comisionVenta = total * 0.0245;
                break;
            default:
                break;
        }

        venta.setComision(comisionVenta);
        venta.setFecha_venta(fecha_venta);
        venta.setMedio_pago(medio_pago);
        venta.setPrecioTotalVenta(total);
        venta.setUsuario(usuarios);

        ventaRepo.save(venta);
    }

    public List<Venta> listarVentas() {

        List<Venta> ventas = new ArrayList();
        ventas = ventaRepo.findAll();

        return ventas;
    }

    @Transactional
    public void modificarVentas(Long num_venta, Date fecha_venta,
            String medio_pago, List<Usuario> usuarios, Long id_servicio,
            Long id_paquete) throws MyException {

        validarDatos(fecha_venta, medio_pago, usuarios);

        Optional<Venta> respuesta = ventaRepo.findById(num_venta);

        if (respuesta.isPresent()) {

            Servicio servicio = servicioRepo.findById(id_servicio)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró ningún servicio con el ID proporcionado"));

            Paquete paquete = paqueteRepo.findById(id_paquete)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró ningún paquete con el ID proporcionado"));

            Venta venta = respuesta.get();
            venta.setFecha_venta(fecha_venta);
            venta.setMedio_pago(medio_pago);
            venta.setUsuario(usuarios);
            venta.setServicio(servicio);
            venta.setPaqueteTuristico(paquete);

            ventaRepo.save(venta);
        }
    }

    private void validarDatos(Date fecha_venta, String medio_pago,
            List<Usuario> usuarios) throws MyException {

        if (medio_pago.isEmpty() || medio_pago == null) {
            throw new MyException("Debe elegir un metodo de pago");
        }
        if (fecha_venta.after(new Date()) || fecha_venta == null) {
            throw new MyException("Fecha incorrecta");
        }
        if (usuarios.isEmpty()) {
            throw new MyException("Debe haber usuarios vinculados");
        }

    }

    public void agregarAlCarrito(Servicio servicio, int cantidad, HttpSession session) {
        List<Servicio> carrito = obtenerCarritoDesdeSesion(session);
        boolean encontrado = false;

        for (Servicio item : carrito) {
            if (item.getCodigo_servicio().equals(servicio.getCodigo_servicio())) {
                item.setCantidad(item.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            // Si el servicio no estaba en el carrito, lo agregamos
            servicio.setCantidad(cantidad);
            carrito.add(servicio);
        }
    }

    public void eliminarDelCarrito(Long servicioId, HttpSession session) {
        List<Servicio> carrito = obtenerCarritoDesdeSesion(session);
        carrito.removeIf(servicio -> servicio.getCodigo_servicio().equals(servicioId));
    }

    public List<Servicio> obtenerCarritoDesdeSesion(HttpSession session) {
        List<Servicio> carrito = (List<Servicio>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    public List<Venta> buscarPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {

        LocalDate fechaActual = LocalDate.now();

        if (fechaFin.isAfter(fechaActual)) {
            // Si la fecha de fin es mayor que la fecha actual, establecerla como la fecha actual
            fechaFin = fechaActual;
        }
        return ventaRepo.buscarPorFecha(fechaInicio, fechaFin);
    }

    public Usuario obtenerMejorVendedor(List<Venta> ventasFiltradas) {
        // Crear un mapa para contar las ventas por vendedor
        Map<Usuario, Integer> ventasPorVendedor = new HashMap<>();

        // Contar las ventas por vendedor
        for (Venta venta : ventasFiltradas) {
            for (Usuario vendedor : venta.getUsuario()) {
                if (vendedor.getRol() == Rol.VENDEDOR) {
                    ventasPorVendedor.put(vendedor, ventasPorVendedor.getOrDefault(vendedor, 0) + 1);
                }
            }
        }

        // Encontrar al vendedor con más ventas
        Usuario mejorVendedor = Collections.max(ventasPorVendedor.entrySet(),
                Map.Entry.comparingByValue()).getKey();

        return mejorVendedor;
    }
    public List<Venta> filtrarVentasPorRolVendedor(List<Venta> ventas) {
        return ventas.stream()
                .filter(venta -> venta.getUsuario().stream()
                        .anyMatch(usuario -> usuario.getRol().equals(Rol.VENDEDOR)))
                .collect(Collectors.toList());
    }
}
