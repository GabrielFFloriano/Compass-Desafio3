package com.example.ecommerce.services;

import java.time.Instant;
import java.util.List;

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.models.Venda;

public interface VendaService {
	VendaDTO criar(VendaDTO vendaDTO);
	VendaDTO obterPorId(Long id);
	VendaDTO atualizar(Long id, VendaDTO vendaDTO);
    void deletar(Long id);
    List<VendaDTO> listar();
    List<VendaDTO> filtrarVendasPorData(Instant startDate, Instant endDate);
    List<VendaDTO> gerarRelatorioSemanal();
    List<VendaDTO> gerarRelatorioMensal();
}
