package com.example.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping
	public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody ProdutoDTO produtoDTO){
		ProdutoDTO produto = service.criar(produtoDTO);
		return ResponseEntity.ok(produto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO){
		ProdutoDTO produto = service.atualizar(id, produtoDTO);
		return ResponseEntity.ok(produto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ProdutoDTO> deletarProduto(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
