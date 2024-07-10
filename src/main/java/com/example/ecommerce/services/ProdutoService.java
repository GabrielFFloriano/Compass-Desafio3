package com.example.ecommerce.services;

import java.util.List;

import com.example.ecommerce.dtos.ProdutoDTO;

public interface ProdutoService {
	ProdutoDTO criar(ProdutoDTO produtoDTO);
	ProdutoDTO obterPorId(Long id);
	ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO);
    void mudarStatus(Long id);
    void deletar(Long id);
    List<ProdutoDTO> listar();
	void limparCacheProdutos();
}
