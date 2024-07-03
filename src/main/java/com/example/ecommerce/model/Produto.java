package com.example.ecommerce.model;


import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Produto {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(nullable = false)
    @NotBlank(message = "Nome do produto é obrigatório")
    private String name;
    
    @Column
    private String descricao;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    private BigDecimal price;

    @Column(nullable = false)
    private boolean ativo = true;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
    
	
}
