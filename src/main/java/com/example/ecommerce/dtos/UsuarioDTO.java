package com.example.ecommerce.dtos;

import java.util.Set;

import com.example.ecommerce.models.Venda;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO (
		Long id,
		@NotBlank(message = "O email é obrigatório")
	    String email,
	    Set<String> roles,
	    Set<Venda> vendas
) {
}
