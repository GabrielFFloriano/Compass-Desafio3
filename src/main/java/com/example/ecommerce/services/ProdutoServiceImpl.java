package com.example.ecommerce.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProdutoMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.Venda;
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
		if (produtoDTO.preco().compareTo(BigDecimal.ZERO)<=0) {
			throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
		}
		Produto produto = mapper.toEntity(produtoDTO);
		produto.setAtivo(true);
		Produto salvo = repository.save(produto);
		return mapper.toDTO(salvo);
	}

	@Override
	@Transactional
	public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
		if (produtoDTO.preco().compareTo(BigDecimal.ZERO)<=0) {
			throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
		}
		
		mapper.updateFromDTO(produtoDTO, produto);
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
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));

        if (!produto.getVendas().isEmpty()) {
            throw new IllegalStateException("Produto não pode ser excluído porque está associado a uma venda.");
        }

        repository.delete(produto);
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
