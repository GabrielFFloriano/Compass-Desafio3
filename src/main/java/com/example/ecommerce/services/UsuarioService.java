package com.example.ecommerce.services;

import java.util.List;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.models.Usuario;

public interface UsuarioService {
	UsuarioDTO criar(UsuarioDTO UsuarioDTO);
	UsuarioDTO obterPorId(Long id);
	UsuarioDTO atualizar(Long id, UsuarioDTO UsuarioDTO);
    void deletar(Long id);
    List<UsuarioDTO> listar();
	Usuario obterPorEmail(String email);
}
