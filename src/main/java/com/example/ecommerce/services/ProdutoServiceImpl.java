package com.example.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.repositories.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	ProdutoRepository repository;
	
	@Override
    @Transactional
	public Produto criar(ProdutoDTO produtoDTO) {
		Produto produto = new Produto();
		produto.setNome(produtoDTO.getNome());
		produto.setPreco(produtoDTO.getPreco());
		produto.setEstoque(produtoDTO.getEstoque());
		return repository.save(produto);
	}


	@Override
    @Transactional
	public Produto atualizar(Long id, ProdutoDTO produtoDTO) {
		Produto produto = obterPorId(id);
		produto.setNome(produtoDTO.getNome());
		produto.setPreco(produtoDTO.getPreco());
		produto.setEstoque(produtoDTO.getEstoque());
		return repository.save(produto);
	}

	@Override
    @Transactional
	public void mudarStatus(Long id) {
		Produto produto = obterPorId(id);
		boolean status = produto.isAtivo()? false : true;
		produto.setAtivo(status);
		repository.save(produto);
	}

	@Override
    @Transactional
	public void deletar(Long id) {
		Produto produto = obterPorId(id);
		//TODO verificar se está numa venda
		repository.delete(produto);
	}

	@Override
	public Produto obterPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
	}

	@Override
	public List<Produto> listar() {
		return repository.findAll();
	}

}
