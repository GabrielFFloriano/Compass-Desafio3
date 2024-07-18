package com.example.ecommerce.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.LoginRequestDTO;
import com.example.ecommerce.dtos.RegisterRequestDTO;
import com.example.ecommerce.dtos.ResponseDTO;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.repositories.UsuarioRepository;
import com.example.ecommerce.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginRequestDTO body) {
        Usuario usuario = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuario not found"));
        if(passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            String token = this.tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getEmail(), token));
        }
        return ResponseEntity.badRequest().build();
	}

	@PostMapping("/register")
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterRequestDTO body) {
		Optional<Usuario> usuario = repository.findByEmail(body.email());
		if (usuario.isPresent())
			return ResponseEntity.badRequest().body(new ResponseDTO("Email já cadastrado", null));

		String encryptedPassword = new BCryptPasswordEncoder().encode(body.senha());
		Usuario newUser = new Usuario();
		newUser.setEmail(body.email());
		newUser.setSenha(encryptedPassword);
		for(Role role : body.roles()) {
			newUser.addRoles(role);
		}
		this.repository.save(newUser);

		return ResponseEntity.ok().build();
	}
}