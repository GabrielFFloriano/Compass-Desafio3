package com.example.ecommerce.services;

public interface EmailService {
	void solicitarResetSenha(String email);

	void resetarSenha(String token, String novaSenha);

}