package com.empresaTurismo.agenciadeturismo.servicio;

import com.empresaTurismo.agenciadeturismo.entidades.Imagen;
import com.empresaTurismo.agenciadeturismo.entidades.Servicio;
import com.empresaTurismo.agenciadeturismo.excepciones.MyException;
import com.empresaTurismo.agenciadeturismo.repositorio.IImagenRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenService {

    @Autowired
    private IImagenRepository imagenRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public Imagen actualizar(String url, Long idImagen, Servicio servicio) throws MyException {
        try {
            Imagen imagen;
            if (idImagen != null) {
                Optional<Imagen> respuesta = imagenRepo.findById(idImagen);
                imagen = respuesta.orElseThrow(() -> new MyException("La imagen no fue encontrada"));
            } else {
                imagen = new Imagen();
            }
            imagen.setUrl(url);
            imagen.setServicio(servicio);
            // Se establece la ruta de la imagen
            imagen.setRutaImagen(obtenerRutaImagen(url));
            // Aquí puedes agregar lógica para manejar la actualización del archivo si es necesario
            return imagenRepo.save(imagen);
        } catch (Exception e) {
            throw new MyException("Error al actualizar la imagen");
        }
    }

    public List<Imagen> listarTodos() {
        return imagenRepo.findAll();
    }

    @Transactional
    public Imagen guardar(String url, Servicio servicio) throws MyException {
        try {
            Imagen imagen = this.guardarDesdeUrl(url);
            imagen.setServicio(servicio);
            return imagenRepo.save(imagen);
        } catch (Exception e) {
            System.out.println("Advertencia: Error al crear el servicio: " + e.getMessage());
        }
        return null;
    }

    @Transactional
    public Imagen guardarDesdeUrl(String url) throws MyException {
        System.out.println("url " + url);
        Imagen imagen = new Imagen();
        
        try {
            
            imagen.setUrl(url);

            // Obtener el contenido de la imagen desde la URL
            URL imageUrl = new URL(url);

            System.out.println("imageUrl " + imageUrl);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestMethod("GET");

            // Configurar tiempo de espera en milisegundos (por ejemplo, 10 segundos)
            connection.setConnectTimeout(30000); // Tiempo de espera para la conexión
            connection.setReadTimeout(30000);    // Tiempo de espera para la lectura de datos

            connection.connect();

            System.out.println("connection " + connection);
            InputStream inputStream = connection.getInputStream();

            System.out.println("inputStream " + inputStream);

            // Guardar la imagen en el sistema de archivos local
            String nombreArchivo = obtenerNombreArchivoDesdeUrl(url);
            String directorioUploads = "C:\\Users\\julid\\OneDrive\\Escritorio\\Agencia Turismo con Spring\\agencia-de-turismo\\src\\main\\resources\\publico\\uploads";
            Path filePath = Paths.get(directorioUploads, nombreArchivo);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

// Se establece la ruta de la imagen
            String rutaImagen = filePath.toString();
            imagen.setRutaImagen(rutaImagen);
            // Establecer los detalles de la imagen en la entidad Imagen
            imagen.setNombre(nombreArchivo);
            imagen.setMime(connection.getContentType());
            // Si es necesario, puedes establecer más atributos de la imagen aquí

            return imagen;

        } catch (MalformedURLException e) {
            System.out.println("Error: La URL proporcionada es incorrecta: " + url);
            // Aquí puedes lanzar una excepción o manejarla de otra manera según tu flujo de programa
            throw new MyException("URL proporcionada incorrecta: " + url);
        } catch (Exception e) {
            // Log o mensaje informativo sobre el error, sin lanzar la excepción hacia arriba
            System.out.println("Advertencia: Error al crear el servicio: " + e.getMessage());
            e.printStackTrace(); // Para obtener detalles del error en la consola
        }
        return imagen;

    }

    private String obtenerNombreArchivoDesdeUrl(String url) {
        // Obtener el índice del último separador de barra ("/") en la URL
        int ultimoSeparadorBarra = url.lastIndexOf("/");

        // Si no se encuentra ningún separador de barra, se devuelve la URL completa
        if (ultimoSeparadorBarra == -1 || ultimoSeparadorBarra == url.length() - 1) {
            return "imagen_desconocida"; // Nombre genérico para casos desconocidos
        }

        // Eliminar los parámetros de consulta, si existen
        String urlSinParametros = url.split("\\?")[0];

        // Extraer el nombre del archivo de la URL sin parámetros de consulta
        String nombreArchivo = urlSinParametros.substring(ultimoSeparadorBarra + 1);

        // Verificar si el nombre de archivo es muy largo
        if (nombreArchivo.length() > 255) {
            // Si es muy largo, truncarlo a una longitud adecuada
            nombreArchivo = nombreArchivo.substring(0, 255);
        }

        return nombreArchivo;
    }

    // Método para obtener la ruta de la imagen desde una URL
    private String obtenerRutaImagen(String url) {
        // Implementa lógica para obtener la ruta de la imagen
        // Por ahora, se devuelve la URL como la ruta de la imagen
        return url;
    }
}
