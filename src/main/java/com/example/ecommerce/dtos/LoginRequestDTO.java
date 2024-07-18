package com.example.ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(@Email(message = "Email deve ser válido") @NotBlank(message = "Email é obrigatório") String email,

		@NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String senha) {

}
