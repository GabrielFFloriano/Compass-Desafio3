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

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.services.VendaService;

@RestController
@RequestMapping("/vendas")
public class VendaController {

	@Autowired
    private VendaService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<VendaDTO> obterVenda(@PathVariable Long id){
		VendaDTO venda = service.obterPorId(id);
		return ResponseEntity.ok(venda);
	}
	
	@GetMapping
	public ResponseEntity<List<VendaDTO>> listarVendas(){
		List<VendaDTO> vendas = service.listar();
		return ResponseEntity.ok(vendas);
	}
	
	@PostMapping
	public ResponseEntity<VendaDTO> criarVenda(@RequestBody VendaDTO vendaDTO){
		VendaDTO venda = service.criar(vendaDTO);
		return ResponseEntity.ok(venda);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO vendaDTO){
		VendaDTO venda = service.atualizar(id, vendaDTO);
		return ResponseEntity.ok(venda);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<VendaDTO> deletarVenda(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
