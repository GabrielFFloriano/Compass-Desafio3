package com.example.ecommerce.controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.services.UsuarioService;
import com.example.ecommerce.services.VendaService;

@RestController
@RequestMapping("/vendas")
public class VendaController {

	@Autowired
	private VendaService service;
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/{id}")
	public ResponseEntity<VendaDTO> obterVenda(@PathVariable Long id) {
		VendaDTO venda = service.obterPorId(id);
		return ResponseEntity.ok(venda);
	}

	@GetMapping
	public ResponseEntity<List<VendaDTO>> listarVendas() {
		List<VendaDTO> vendas = service.listar();
		return ResponseEntity.ok(vendas);
	}

	@GetMapping("/filtrar")
	public ResponseEntity<List<VendaDTO>> filtrarVendasPorData(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
		List<VendaDTO> vendas = service.filtrarVendasPorData(startDate, endDate);
		return ResponseEntity.ok(vendas);
	}

	@GetMapping("/relatorio/semanal")
	public ResponseEntity<List<VendaDTO>> gerarRelatorioSemanal() {
		List<VendaDTO> vendas = service.gerarRelatorioSemanal();
		return ResponseEntity.ok(vendas);
	}

	@GetMapping("/relatorio/mensal")
	public ResponseEntity<List<VendaDTO>> gerarRelatorioMensal() {
		List<VendaDTO> vendas = service.gerarRelatorioMensal();
		return ResponseEntity.ok(vendas);
	}

	@PostMapping
	public ResponseEntity<VendaDTO> criarVenda(@RequestBody VendaDTO vendaDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUsuario = authentication.getName();
		Usuario usuario = usuarioService.obterPorEmail(emailUsuario);
		VendaDTO venda = service.criar(vendaDTO, usuario);
		return ResponseEntity.ok(venda);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO vendaDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUsuario = authentication.getName();
		Usuario usuario = usuarioService.obterPorEmail(emailUsuario);
		VendaDTO venda = service.atualizar(id, vendaDTO, usuario);
		return ResponseEntity.ok(venda);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<VendaDTO> deletarVenda(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUsuario = authentication.getName();
		Usuario usuario = usuarioService.obterPorEmail(emailUsuario);
		service.deletar(id, usuario);
		return ResponseEntity.noContent().build();
	}
}
