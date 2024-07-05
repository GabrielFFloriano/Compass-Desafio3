package com.example.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
    private ProdutoService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDTO> obterProduto(@PathVariable Long id){
		ProdutoDTO produto = service.obterPorId(id);
		return ResponseEntity.ok(produto);
	}
	
	@GetMapping
	public ResponseEntity<List<ProdutoDTO>> listarProdutos(){
		List<ProdutoDTO> produtos = service.listar();
		return ResponseEntity.ok(produtos);
	}
}
