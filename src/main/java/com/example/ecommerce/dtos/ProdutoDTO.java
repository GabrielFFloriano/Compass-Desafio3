package com.example.ecommerce.dtos;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProdutoDTO(
    Long id,
    
    @NotBlank(message = "Nome do produto é obrigatório")
    String nome,
    
    String descricao,
    
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    BigDecimal preco,
    
    boolean ativo,
    
    @Min(value = 0, message = "Estoque do produto não pode ser negativo")
    Integer estoque
) {
}