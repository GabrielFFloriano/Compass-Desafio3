package com.example.ecommerce.services;

import java.time.Instant;
import java.util.List;

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.models.Usuario;

public interface VendaService {
	VendaDTO criar(VendaDTO vendaDTO, Usuario usuario);
	VendaDTO obterPorId(Long id);
	VendaDTO atualizar(Long id, VendaDTO vendaDTO, Usuario usuario);
    void deletar(Long id, Usuario usuario);
    List<VendaDTO> listar();
    List<VendaDTO> filtrarVendasPorData(Instant startDate, Instant endDate);
    List<VendaDTO> gerarRelatorioSemanal();
    List<VendaDTO> gerarRelatorioMensal();
}
