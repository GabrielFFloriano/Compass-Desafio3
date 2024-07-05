package com.example.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProdutoMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.repositories.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	@Autowired
	private ProdutoMapper mapper;

	@Override
	@Transactional
	public ProdutoDTO criar(ProdutoDTO produtoDTO) {
		Produto produto = mapper.toEntity(produtoDTO);
		Produto salvo = repository.save(produto);
		return mapper.toDTO(salvo);
	}

	@Override
	@Transactional
	public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
		produto.setNome(produtoDTO.nome());
	    produto.setDescricao(produtoDTO.descricao()); 
		produto.setPreco(produtoDTO.preco());
		produto.setEstoque(produtoDTO.estoque());
		Produto atualizado = repository.save(produto);
		return mapper.toDTO(atualizado);
	}

	@Override
	@Transactional
	public void mudarStatus(Long id) {
		Produto produto = mapper.toEntity(obterPorId(id));
		boolean status = produto.isAtivo() ? false : true;
		produto.setAtivo(status);
		repository.save(produto);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		// TODO verificar se está numa venda
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new ResourceNotFoundException("Produto não encontrado com o ID: " + id);
		}
	}

	@Override
	public ProdutoDTO obterPorId(Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		return mapper.toDTO(produto);
	}

	@Override
	public List<ProdutoDTO> listar() {
		List<Produto> produtos = repository.findAll();
		return produtos.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

}
