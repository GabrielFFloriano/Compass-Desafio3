package com.example.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.repositories.UsuarioRepository;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JavaMailSender javaMailSender;
    
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
    public void solicitarResetSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));

        String token = tokenService.gerarToken(usuario);

        enviarEmailResetSenha(usuario.getEmail(), token);
    }
	
    private void enviarEmailResetSenha(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset de Senha");
        message.setText("Para resetar sua senha, utilize o seguinte token: " + token);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage(), e);
        }
    }
	
    @Override
    public void resetarSenha(String token, String novaSenha) {
        String email = tokenService.validarToken(token);

        if (email == null) {
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }
}
