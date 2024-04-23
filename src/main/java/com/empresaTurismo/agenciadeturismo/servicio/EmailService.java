package com.empresaTurismo.agenciadeturismo.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
     @Autowired
    private JavaMailSender javaMailSender;
     
       public void enviarCorreo(String contactoNombre, String contactoEmail, String contactomensaje) {
        if (contactoNombre == null || contactoNombre.isEmpty() ||
            contactoEmail == null || contactoEmail.isEmpty() ||
            contactomensaje == null || contactomensaje.isEmpty()) {
            throw new IllegalArgumentException("Los parámetros de contacto no pueden estar vacíos");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        String mensaje = "Saludos,\n\nSe recibió un nuevo mensaje de " + contactoNombre + " a responder al email: " + contactoEmail 
                        + "\n\n---------------------------------\n\n" + contactomensaje + "\n\n---------------------------------\n\nAtt: FiveDesign";

        mailMessage.setTo("horizontesviajeros5@gmail.com");
        mailMessage.setSubject("Nuevo mensaje de contacto");
        mailMessage.setText(mensaje);

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            // Manejar la excepción de envío de correo
            System.err.println("Error al enviar correo: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo. Intente nuevamente más tarde.");
        }
    }
}
