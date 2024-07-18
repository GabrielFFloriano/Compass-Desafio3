package com.example.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.UsuarioMapper;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public UsuarioDTO criar(UsuarioDTO UsuarioDTO) {
		Usuario usuario = mapper.toEntity(UsuarioDTO);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		Usuario salvo = repository.save(usuario);
		return mapper.toDTO(salvo);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public UsuarioDTO atualizar(Long id, UsuarioDTO UsuarioDTO) {
		Usuario usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o ID: " + id));
		mapper.updateFromDTO(UsuarioDTO, usuario);;
		Usuario atualizado = repository.save(usuario);
		return mapper.toDTO(atualizado);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public void deletar(Long id) {
		Usuario usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado: " + id));
		if (!usuario.getVendas().isEmpty()) {
            throw new IllegalStateException("Usuário não pode ser excluído porque está associado a vendas.");
        }
		repository.delete(usuario);
	}

	@Override
	@Transactional
	@Cacheable(value = "usuariosCache", key = "#id")
	public UsuarioDTO obterPorId(Long id) {
		Usuario usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o ID: " + id));
		return mapper.toDTO(usuario);
	}

	@Override
	@Transactional
	@Cacheable(value = "usuariosCache")
	public List<UsuarioDTO> listar() {
		List<Usuario> usuarios = repository.findAll();
		
		return usuarios.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	@Cacheable(value = "usuariosCache", key = "#email")
	public Usuario obterPorEmail(String email) {
		 return repository.findByEmail(email)
				 .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o email: " + email));
	    }
}
