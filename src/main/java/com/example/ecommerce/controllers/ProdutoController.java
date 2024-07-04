package com.example.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.models.Produto;
import com.example.ecommerce.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
    private ProdutoService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable Long id){
		Produto produto = service.obterPorId(id);
		return ResponseEntity.ok(produto);
	}
	
}
