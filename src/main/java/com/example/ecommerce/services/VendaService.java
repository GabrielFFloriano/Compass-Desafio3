package com.example.ecommerce.services;

import java.util.List;

import com.example.ecommerce.dtos.VendaDTO;

public interface VendaService {
	VendaDTO criar(VendaDTO vendaDTO);
	VendaDTO obterPorId(Long id);
	VendaDTO atualizar(Long id, VendaDTO vendaDTO);
    void deletar(Long id);
    List<VendaDTO> listar();
}
