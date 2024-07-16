package com.example.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
    private UsuarioService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> obterUsuario(@PathVariable Long id){
		UsuarioDTO usuario = service.obterPorId(id);
		return ResponseEntity.ok(usuario);
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios(){
		List<UsuarioDTO> usuarios = service.listar();
		return ResponseEntity.ok(usuarios);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO){
		UsuarioDTO usuario = service.criar(usuarioDTO);
		return ResponseEntity.ok(usuario);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO){
		UsuarioDTO usuario = service.atualizar(id, usuarioDTO);
		return ResponseEntity.ok(usuario);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<UsuarioDTO> deletarUsuario(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
