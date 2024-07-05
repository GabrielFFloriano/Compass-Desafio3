package com.example.ecommerce.models;


import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Produto {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(nullable = false)
    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;
    
    @Column
    private String descricao;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    private BigDecimal preco;

    @Column(nullable = false)
    @Min(value = 0, message = "Estoque do produto não pode ser negativo")
    private Integer estoque;
    
    @Column(nullable = false)
    private boolean ativo = true;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

    
	
}
