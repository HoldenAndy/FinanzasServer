package com.example.proyecto1.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarEmailConfirmacion(String emailDestino, String codigo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(emailDestino);
        helper.setSubject("Confirmar tu cuenta - Finanzas App");

        String urlConfirmacion = "http://localhost:8080/auth/confirmar?codigo=" + codigo;

        String contenidoHtml = "<h3>¡Bienvenido a Finanzas App!</h3>" +
                "<p>Por favor, haz clic en el botón de abajo para verificar tu correo:</p>" +
                "<a href='" + urlConfirmacion + "' style='background: #28a745; color: white; padding: 10px; text-decoration: none;'>Verificar mi cuenta</a>";

        helper.setText(contenidoHtml, true);
        mailSender.send(mensaje);
    }
}