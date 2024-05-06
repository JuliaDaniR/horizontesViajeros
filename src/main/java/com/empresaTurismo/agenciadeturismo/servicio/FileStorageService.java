package com.empresaTurismo.agenciadeturismo.servicio;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    
    private final String uploadDir = "C:\\Users\\julid\\OneDrive\\Escritorio\\Agencia Turismo con Spring\\agencia-de-turismo\\src\\main\\resources\\publico\\uploads";
    
    public void storeFile(MultipartFile file, String fileName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath);
        } catch (IOException ex) {
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
