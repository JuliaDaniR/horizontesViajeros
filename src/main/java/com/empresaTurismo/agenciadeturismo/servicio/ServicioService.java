package com.empresaTurismo.agenciadeturismo.servicio;

import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.repositorio.IImagenRepository;
import com.empresaTurismo.agenciadeturismo.repositorio.IPaqueteRepository;
import com.empresaTurismo.agenciadeturismo.repositorio.IServicioRepository;
import com.empresaTurismo.agenciadeturismo.entidades.Imagen;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.enumerador.TipoServicio;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.repositorio.IUsuarioRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class ServicioService {

    @Autowired
    private IServicioRepository servicioRepo;

    @Autowired
    private IPaqueteRepository paqueteRepo;

    @Autowired
    private IImagenRepository imagenRepo;

    @Autowired
    private IUsuarioRepository usuarioRepo;

    @Autowired
    private ImagenService imagenService;

    @Transactional
    public void crearServicio(String nombre,
            String descripcion_breve,
            String destino_servicio,
            String pais_destino,
            Date fecha_servicio,
            Double costo_servicio,
            TipoServicio tipoServicio,
            Long id_vendedor,
            List<String> urls_imagenes) throws MyException {

        List<Imagen> imagenes = new ArrayList<>();
        try {
            // Crear y configurar el servicio
            Servicio servicio = new Servicio();
            Usuario vendedor = obtenerVendedor(id_vendedor);

            servicio.setNombre(nombre);
            servicio.setDescripcion_breve(descripcion_breve);
            servicio.setDestino_servicio(destino_servicio);
            servicio.setPais_destino(pais_destino);
            servicio.setFecha_servicio(fecha_servicio);
            servicio.setCosto_servicio(costo_servicio);
            servicio.setTipoServicio(tipoServicio);
            servicio.setVendedor(vendedor);
            servicio.setCantidad(0);
            servicio.setVisitas(0);

            // Guardar el servicio para obtener el ID
            servicio = servicioRepo.save(servicio);

            // Asociar las imágenes con el servicio
            for (String url : urls_imagenes) {
                Imagen imagen = imagenService.guardar(url, servicio);
                if (imagen != null) {
                    imagenes.add(imagen);
                } else {
                    // Log o mensaje informativo sobre la imagen no guardada, pero sin lanzar excepción
                    System.out.println("Advertencia: La imagen desde la URL no se pudo guardar correctamente: " + url);
                }
            }

            // Asignar las imágenes al servicio
            servicio.setImagenes(imagenes);

            // Guardar el servicio con las imágenes asociadas
            servicioRepo.save(servicio);

            // Mostrar mensaje de éxito
            System.out.println("El servicio se creó correctamente junto con las imágenes asociadas.");
        } catch (Exception e) {
            throw new MyException("Error al crear el servicio: " + e.getMessage());
        }
    }

    private Usuario obtenerVendedor(Long id_vendedor) throws MyException {
        // Obtener el usuario por su ID
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(id_vendedor);

        // Verificar si se encontró el usuario con el ID proporcionado
        Usuario usuario = usuarioOptional.orElseThrow(() -> new MyException("No se encontró ningún vendedor con el ID proporcionado"));

        // Obtener el nombre de usuario y verificar si es un administrador o el vendedor correcto
        String email = usuario.getEmail();
        Usuario vendedor = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new MyException("No se encontró ningún vendedor con el ID proporcionado"));

        if (vendedor.getRol() == Rol.ADMIN || vendedor.getId_usuario().equals(id_vendedor)) {
            return vendedor;
        } else {
            throw new MyException("No tienes permisos para asignar servicios a otro vendedor");
        }
    }

    public List<Servicio> listarServicios() {

        List<Servicio> servicios = new ArrayList();
        servicios = servicioRepo.findAll();

        return servicios;
    }

    @Transactional
    public void modificarServicio(Long id_servicio, String nombre, String descripcion_breve,
            String destino_servicio, String pais_destino, Date fecha_servicio,
            Double costo_servicio, TipoServicio tipoServicio, Long id_vendedor,
            List<Long> id_imagenes, List<String> url_imagenes) throws MyException, ProtocolException, IOException {

        // Obtener el servicio a modificar
        Optional<Servicio> optionalServicio = servicioRepo.findById(id_servicio);
        if (optionalServicio.isEmpty()) {
            throw new NoSuchElementException("No se encontró ningún servicio con el ID proporcionado.");
        }
        Servicio servicio = optionalServicio.get();

        // Verificar si todas las imágenes existentes proporcionadas existen en la base de datos
        List<Imagen> imagenes = new ArrayList<>();
        if (id_imagenes != null) {
            imagenes = imagenRepo.findAllById(id_imagenes);
            if (imagenes.size() != id_imagenes.size()) {
                throw new MyException("Una o más imágenes existentes no se encontraron en la base de datos.");
            }
        }

        // Guardar las nuevas imágenes desde las URLs proporcionadas
        for (String url : url_imagenes) {
            Imagen nuevaImagen = imagenService.guardarDesdeUrl(url);
            if (nuevaImagen != null) {
                nuevaImagen.setServicio(servicio);
                imagenes.add(nuevaImagen);
            } else {
                // Log o mensaje informativo sobre la imagen no guardada correctamente
                System.out.println("Advertencia: La imagen desde la URL no se pudo guardar correctamente: " + url);
            }
        }

        // Verificar si el usuario autenticado tiene permisos para modificar el servicio
        validarPermisosParaModificar(servicio, id_vendedor);

        // Asignar los nuevos valores al servicio
        servicio.setNombre(nombre);
        servicio.setDescripcion_breve(descripcion_breve);
        servicio.setDestino_servicio(destino_servicio);
        servicio.setPais_destino(pais_destino);
        servicio.setFecha_servicio(fecha_servicio);
        servicio.setCosto_servicio(costo_servicio);
        servicio.setTipoServicio(tipoServicio);
        servicio.setImagenes(imagenes);

        // Guardar el servicio actualizado en la base de datos
        servicioRepo.save(servicio);
    }

    private void validarPermisosParaModificar(Servicio servicio, Long id_vendedor) throws MyException {
        // Verificar si el servicio pertenece al vendedor que intenta modificarlo
        if (!Objects.equals(servicio.getVendedor().getId_usuario(), id_vendedor)) {
            throw new MyException("No tienes permisos para modificar este servicio.");
        }
        // Puedes agregar otras validaciones según tus reglas de negocio, roles, etc.
    }

    private void validarDatos(Long id_servicio,
            String nombre,
            String descripcion_breve,
            String destino_servicio,
            String pais_destino,
            Date fecha_servicio,
            Double costo_servicio) throws MyException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede estar vacio");
        }
        if (descripcion_breve.isEmpty() || descripcion_breve == null) {
            throw new MyException("La descripcion no puede estar vacia");
        }
        if (destino_servicio.isEmpty() || destino_servicio == null) {
            throw new MyException("El destino no puede estar vacio");
        }
        if (pais_destino.isEmpty() || pais_destino == null) {
            throw new MyException("El pais no puede estar vacio");
        }
        if (fecha_servicio.before(new Date()) || fecha_servicio == null) {
            throw new MyException("Fecha incorrecta");
        }
        if (costo_servicio == null) {
            throw new MyException("El costo no puede ser nulo");
        }

    }

    public Servicio obtenerPorId(Long servicioId) {

        Optional<Servicio> servicioOptional = servicioRepo.findById(servicioId);
        return servicioOptional.orElse(null);

    }

    public List<Servicio> listarServiciosPorVendedor(Usuario vendedor) {
        return servicioRepo.findByVendedor(vendedor);
    }

    public void actualizarCantidadServicio(Servicio servicio) {

        // Obtener el servicio existente de la base de datos
        Servicio servicioExistente = servicioRepo.findById(servicio.getCodigo_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Actualizar solo los campos necesarios
        servicioExistente.setCantidad(servicio.getCantidad());
        // Agregar más campos aquí si es necesario actualizarlos

        // Guardar el servicio actualizado en la base de datos
        servicioRepo.save(servicioExistente);
    }

    public void actualizarVisitasServicio(Servicio servicio) {
        // Obtener el servicio existente de la base de datos
        Servicio servicioExistente = servicioRepo.findById(servicio.getCodigo_servicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Actualizar solo los campos necesarios
        servicioExistente.setVisitas(servicio.getVisitas() + 5);

        // Guardar el servicio actualizado en la base de datos
        servicioRepo.save(servicioExistente);
    }

    public List<Servicio> listarMasVendidos() {
        return servicioRepo.buscarMasVendidos();
    }

    public List<Servicio> listarMasVisitados() {
        return servicioRepo.buscarMasVisitados();
    }

    public List<Servicio> listarPorTipoServicio(TipoServicio tipoServicio) {
        return servicioRepo.buscarPorTipoServicio(tipoServicio);
    }

    public List<Servicio> filtrarTipoPaisVisitas(TipoServicio tipoServicio, String pais_destino, String ordenVisitas) {
        return servicioRepo.filtrarTipoPaisVisitas(tipoServicio, pais_destino);
    }

    public List<Servicio> filtrarTipoPaisCantidad(TipoServicio tipoServicio, String pais_destino, String ordenCantidad) {
        return servicioRepo.filtrarTipoPaisCantidad(tipoServicio, pais_destino);
    }

    public List<Servicio> filtrarPaisVisitas(String pais_destino, String ordenVisitas) {
        return servicioRepo.filtrarPaisVisitas(pais_destino);
    }

    public List<Servicio> filtrarPaisCantidad(String pais_destino, String ordenCantidad) {
        return servicioRepo.filtrarPaisCantidad(pais_destino);
    }

    public List<Servicio> filtrarTipoVisitas(TipoServicio tipoServicio, String ordenVisitas) {
        return servicioRepo.filtrarTipoVisitas(tipoServicio);
    }

    public List<Servicio> filtrarTipoCantidad(TipoServicio tipoServicio, String ordenCantidad) {
        return servicioRepo.filtrarTipoCantidad(tipoServicio);
    }

    public List<Servicio> listarPorPais(String pais_destino) {
        System.out.println("***********servicio pais " + pais_destino);
        return servicioRepo.listarPorPais(pais_destino);
    }

    @Transactional
    public void eliminarPorId(Long id) {
        Optional<Servicio> optionalServicio = servicioRepo.findById(id);
        optionalServicio.ifPresent(servicio -> {
            servicioRepo.delete(servicio);
            servicioRepo.flush(); // Forzar la sincronización con la base de datos
        });
    }

    @Transactional
    public void agregarImagenesAlServicio(Servicio servicioExistente, List<String> urls_imagenes) {
        // Crear objetos Imagen a partir de las URLs y asociarlos al servicio
        List<Imagen> nuevasImagenes = (List<Imagen>) urls_imagenes.stream()
                .map(url -> new Imagen(servicioExistente, url))
                .collect(Collectors.toList());

        // Guardar las nuevas imágenes asociadas al servicio
        servicioExistente.getImagenes().addAll(nuevasImagenes);
        servicioRepo.save(servicioExistente);
    }

    @Transactional
    public void modificarServicioYImagenes(Servicio servicioExistente, String nombre, String descripcion_breve,
            String destino_servicio, String pais_destino, Date fecha_servicio,
            Double costo_servicio, TipoServicio tipoServicio, Long id_usuario,
            List<Long> idList, List<String> urls_imagenes) {
        // Modificar los campos del servicio
        servicioExistente.setNombre(nombre);
        servicioExistente.setDescripcion_breve(descripcion_breve);
        servicioExistente.setDestino_servicio(destino_servicio);
        servicioExistente.setPais_destino(pais_destino);
        servicioExistente.setFecha_servicio(fecha_servicio);
        servicioExistente.setCosto_servicio(costo_servicio);
        servicioExistente.setTipoServicio(tipoServicio);
        // Puedes continuar con la actualización de otros campos

        // Modificar las imágenes existentes si se proporcionan IDs válidos
        if (!idList.isEmpty()) {
            for (Imagen imagen : servicioExistente.getImagenes()) {
                if (idList.contains(imagen.getId())) {
                    // Modificar la imagen si se encuentra en la lista de IDs
                    // Aquí puedes actualizar la imagen si es necesario
                }
            }
        }

        // Agregar nuevas imágenes al servicio si se proporcionan URLs de imágenes nuevas
        if (!urls_imagenes.isEmpty()) {
            agregarImagenesAlServicio(servicioExistente, urls_imagenes);
        }

        // Guardar los cambios en el servicio
        servicioRepo.save(servicioExistente);
    }
}
