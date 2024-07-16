package com.example.ecommerce.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProdutoMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.repositories.ProdutoRepository;
import com.example.ecommerce.repositories.VendaRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private ProdutoMapper mapper;

	@Override
	@Transactional
	@CacheEvict(value = "produtosCache", allEntries = true)
	public ProdutoDTO criar(ProdutoDTO produtoDTO) {
		if (produtoDTO.preco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
		}
		Produto produto = mapper.toEntity(produtoDTO);
		produto.setAtivo(true);
		Produto salvo = repository.save(produto);
		return mapper.toDTO(salvo);
	}

	@Override
	@Transactional
	@CacheEvict(value = "produtosCache", allEntries = true)
	public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
		if (produtoDTO.preco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
		}

		boolean status = produto.isAtivo();
		mapper.updateFromDTO(produtoDTO, produto);
		produto.setAtivo(status);
		Produto atualizado = repository.save(produto);
		return mapper.toDTO(atualizado);
	}

	@Override
	@Transactional
	@CacheEvict(value = "produtosCache", allEntries = true)
	public void mudarStatus(Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
		produto.setAtivo(!produto.isAtivo());
		List<Venda> vendas = vendaRepository.findByProdutosId(id);
		for (Venda venda : vendas) {
			BigDecimal total = venda.getProdutos().stream().map(vendaProduto -> {
				Produto prod = vendaProduto.getProduto();
				int quantidade = vendaProduto.getQuantidade();
				return prod.isAtivo() ? prod.getPreco().multiply(BigDecimal.valueOf(quantidade)) : BigDecimal.ZERO;
			}).reduce(BigDecimal.ZERO, BigDecimal::add);
			venda.setTotal(total);
			vendaRepository.save(venda);
		}
		repository.save(produto);
	}

	@Override
	@Transactional
	@CacheEvict(value = "produtosCache", allEntries = true)
	public void deletar(Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));

		if (!produto.getVendas().isEmpty()) {
			throw new IllegalStateException("Produto não pode ser excluído porque está associado a uma venda.");
		}

		repository.delete(produto);
	}

	@Override
	@Cacheable(value = "produtosCache", key = "#id")
	public ProdutoDTO obterPorId(Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
		return mapper.toDTO(produto);
	}

	@Override
	@Cacheable(value = "produtosCache")
	public List<ProdutoDTO> listar() {
		List<Produto> produtos = repository.findAll();
		return produtos.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

}
