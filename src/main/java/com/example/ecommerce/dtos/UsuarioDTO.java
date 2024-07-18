package com.example.ecommerce.dtos;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO (
		Long id,
		@NotBlank(message = "O email é obrigatório")
	    String email,
	    Set<String> roles,
	    Set<Long> vendasIds
) {
}
