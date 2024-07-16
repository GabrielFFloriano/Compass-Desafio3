package com.example.ecommerce.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.UsuarioMapper;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.repositories.UsuarioRepository;
import com.example.ecommerce.repositories.VendaRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private UsuarioMapper mapper;

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public UsuarioDTO criar(UsuarioDTO UsuarioDTO) {
		Usuario Usuario = mapper.toEntity(UsuarioDTO);
		
		Usuario salvo = repository.save(Usuario);
		return mapper.toDTO(salvo);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public UsuarioDTO atualizar(Long id, UsuarioDTO UsuarioDTO) {
		Usuario Usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o ID: " + id));
		mapper.updateFromDTO(UsuarioDTO, Usuario);;
		Usuario atualizado = repository.save(Usuario);
		return mapper.toDTO(atualizado);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usuariosCache", allEntries = true)
	public void deletar(Long id) {
		Usuario Usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado: " + id));

//		if (!Usuario.getVendas().isEmpty()) {
//			throw new IllegalStateException("Usuario não pode ser excluído porque está associado a uma venda.");
//		}

		repository.delete(Usuario);
	}

	@Override
	@Cacheable(value = "usuariosCache", key = "#id")
	public UsuarioDTO obterPorId(Long id) {
		Usuario Usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o ID: " + id));
		return mapper.toDTO(Usuario);
	}

	@Override
	@Cacheable(value = "usuariosCache")
	public List<UsuarioDTO> listar() {
		List<Usuario> Usuarios = repository.findAll();
		return Usuarios.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

}
