package com.example.ecommerce.dtos;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;

    private String descricao;

    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    private BigDecimal preco;

    @Min(value = 0, message = "Estoque do produto não pode ser negativo")
    private Integer estoque;
    
    private boolean ativo;

    public ProdutoDTO() {}

    public ProdutoDTO(Long id, String nome, String descricao, BigDecimal preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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