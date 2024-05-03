package com.empresaTurismo.agenciadeturismo.servicio;

import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.entidades.Usuario;
import com.empresaTurismo.agenciadeturismo.enumerador.Rol;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.repositorio.IServicioRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.empresaTurismo.agenciadeturismo.repositorio.IUsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

    @Autowired
    private IUsuarioRepository usuarioRepo;

    @Autowired
    private IServicioRepository servicioRepo;


    @Transactional
    public void crearUsuario(String nombre, String apellido, String direccion,
            String dni, Date fecha_nac, String nacionalidad, Long celular,
            String email, String cargo, Double sueldo,
            Rol rol, String password, String password2) throws MyException {

        validarDatos(nombre, apellido, direccion, dni,
                fecha_nac, nacionalidad, celular, email,
                cargo, sueldo, rol, password);
        valida(password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDireccion(direccion);
        usuario.setDni(dni);
        usuario.setFecha_nac(fecha_nac);
        usuario.setNacionalidad(nacionalidad);
        usuario.setCelular(celular);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        List <Usuario> lista = usuarioRepo.findAll();
        if(lista.isEmpty()){
            usuario.setRol(Rol.ADMIN);
        }else{
        usuario.setRol(rol);
        }
        usuario.setCargo(cargo);
        usuario.setSueldo(sueldo);
        if (rol.equals(Rol.VENDEDOR)) {
            usuario.setEstado(false);
        } else {
            usuario.setEstado(Boolean.TRUE);
        }
        usuarioRepo.save(usuario);
    }

    public List<Usuario> listarClientes() {

        List<Usuario> usua = usuarioRepo.findAll();
        List<Usuario> clientes = new ArrayList();
        for (Usuario usuario : usua) {
            if (usuario.getRol().equals(Rol.CLIENTE)) {
                clientes.add(usuario);
            }
        }
        return clientes;
    }

    public List<Usuario> listarEmpleados() {

        List<Usuario> usua = usuarioRepo.findAll();
        List<Usuario> empleados = new ArrayList();
        for (Usuario usuario : usua) {
            if (usuario.getRol().equals(Rol.VENDEDOR)) {
                empleados.add(usuario);
            }
        }
        return empleados;
    }

    @Transactional
    public void modificarUsuario(Long id_usuario, String nombre, String apellido, String direccion,
            String dni, Date fecha_nac, String nacionalidad, Long celular,
            String email, String cargo, Double sueldo,
            Rol rol, String password, Boolean estado) throws MyException {

          System.out.println("ID de usuario: " + id_usuario);
    System.out.println("Nombre: " + nombre);
    System.out.println("Apellido: " + apellido);
    System.out.println("Dirección: " + direccion);
    System.out.println("DNI: " + dni);
    System.out.println("Fecha de nacimiento: " + fecha_nac);
    System.out.println("Nacionalidad: " + nacionalidad);
    System.out.println("Celular: " + celular);
    System.out.println("Email: " + email);
    System.out.println("Cargo: " + cargo);
    System.out.println("Sueldo: " + sueldo);
    System.out.println("Rol: " + rol);
    System.out.println("Contraseña: " + password);
    System.out.println("Estado: " + estado);
        
        validarDatos(nombre, apellido, direccion, dni, fecha_nac,
                nacionalidad, celular, email, cargo, sueldo, rol,
                password);

        Optional<Usuario> respuesta = usuarioRepo.findById(id_usuario);

        System.out.println("respuesta "+respuesta);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDireccion(direccion);
            usuario.setDni(dni);
            usuario.setFecha_nac(fecha_nac);
            usuario.setNacionalidad(nacionalidad);
            usuario.setCelular(celular);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setEmail(email);
            usuario.setRol(rol);
            usuario.setCargo(cargo);
            usuario.setSueldo(sueldo);
            usuario.setEstado(estado);

            usuarioRepo.save(usuario);
        }
    }

    @Transactional
    public void vincularServicio(Long idUsuario, Long idServicio) throws MyException {
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(idUsuario);
        Optional<Servicio> servicioOptional = servicioRepo.findById(idServicio);

        if (usuarioOptional.isPresent() && servicioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Servicio servicio = servicioOptional.get();

            usuario.getServicios().add(servicio); // Agregar el servicio al usuario
            usuarioRepo.save(usuario); // Guardar el usuario actualizado en la base de datos
        } else {
            throw new MyException("No se encontró el usuario o el servicio especificado");
        }
    }

    public void valida(String password, String password2) throws MyException {
        if (!password.equals(password2)) {
            throw new MyException("los passwords deben ser iguales ");
        }
    }

    private void validarDatos(String nombre, String apellido, String direccion,
            String dni, Date fecha_nac, String nacionalidad, Long celular,
            String email, String cargo, Double sueldo,
            Rol rol, String password) throws MyException {

        if (nombre.isEmpty()) {
            throw new MyException("El nombre no puede estar vacio");
        }
        if (apellido.isEmpty()) {
            throw new MyException("El apellido no puede estar vacio");
        }
        if (direccion.isEmpty()) {
            throw new MyException("La direccion no puede estar vacio");
        }
        if (dni == null) {
            throw new MyException("El dni no puede estar vacio");
        }
        if (fecha_nac == null || fecha_nac.after(new Date())) {
            throw new MyException("Fecha incorrecta");
        }
        if (nacionalidad.isEmpty()) {
            throw new MyException("La nacionalidad no puede estar vacio");
        }
        if (celular == null) {
            throw new MyException("El numero de celular no puede ser nulo");
        }
        if (email.isEmpty()) {
            throw new MyException("El email no puede estar vacio");
        }
        if (Rol.VENDEDOR.equals(rol)) {
            if (sueldo == null) {
                throw new MyException("El sueldo no puede ser nulo");
            }
            if (cargo.isEmpty()) {
                throw new MyException("El email no puede estar vacio");
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(email);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println(" role" + usuario.getRol().name());

        if (usuario != null && usuario.getEstado().toString().equalsIgnoreCase("true")) {

            List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepo.findByRol(rol);
    }

    //Cambiar estado
    public void cambiarEstado(Usuario usuario) {
        usuario.setEstado(!usuario.getEstado());
        usuarioRepo.save(usuario);

    }

    //agrego metodo para detectar si hay usuarios sin activar    
    @Transactional
    public Integer Inactivos() {

        Integer contador = 0;

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepo.findAll();

        for (Usuario usuario : usuarios) {

            if (usuario.getEstado().toString().equalsIgnoreCase("FALSE")) {
                contador++;
            }
        }
        return contador;

    }

    public boolean isUsuarioAlta(String email) {

        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);
        return usuarioOpt.isPresent() && usuarioOpt.get().getEstado() != null && usuarioOpt.get().getEstado();
    }

    public Usuario buscarUsuario(Long id_usuario) {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById(id_usuario);

        return optionalUsuario.orElse(null); // Retorna null si no se encuentra el usuario
    }

    public List<Usuario> listaUsuarios() {

        List<Usuario> usua = usuarioRepo.findAll();

        return usua;
    }
}
