package com.example.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.VendaMapper;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.repositories.VendaRepository;

import jakarta.transaction.Transactional;

@Service
public class VendaServiceImpl implements VendaService {

	@Autowired
	private VendaRepository repository;

	@Autowired
	private VendaMapper mapper;

	@Override
	@Transactional
	public VendaDTO criar(VendaDTO vendaDTO) {
		// TODO (Uma venda tem que ter no mínimo 1 produto para ser concluída)
		Venda venda = mapper.toEntity(vendaDTO);
		Venda salva = repository.save(venda);
		return mapper.toDTO(salva);
	}

	@Override
	@Transactional
	public VendaDTO atualizar(Long id, VendaDTO vendaDTO) {
		Venda venda = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com o ID: " + id));
		venda.setTotal(vendaDTO.total());
		Venda atualizado = repository.save(venda);
		return mapper.toDTO(atualizado);
	}


	@Override
	@Transactional
	public void deletar(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new ResourceNotFoundException("Venda não encontrada com o ID: " + id);
		}
	}

	@Override
	public VendaDTO obterPorId(Long id) {
		Venda venda = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
		return mapper.toDTO(venda);
	}

	@Override
	public List<VendaDTO> listar() {
		List<Venda> vendas = repository.findAll();
		return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

}
