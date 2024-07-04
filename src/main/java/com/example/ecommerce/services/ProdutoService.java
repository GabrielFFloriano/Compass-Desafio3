package com.example.ecommerce.services;

import java.util.List;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.models.Produto;

public interface ProdutoService {
	Produto criar(ProdutoDTO produtoDTO);
    Produto obterPorId(Long id);
    Produto atualizar(Long id, ProdutoDTO produtoDTO);
    void mudarStatus(Long id);
    void deletar(Long id);
    List<Produto> listar();
}
