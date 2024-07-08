package com.example.ecommerce.dtos;
import java.math.BigDecimal;
import java.util.Set;

import com.example.ecommerce.models.Venda;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProdutoDTO(
    Long id,
    
    @NotBlank(message = "Nome do produto é obrigatório")
    String nome,
    
    String descricao,
    
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    @Digits(integer = 10, fraction = 2, message = "Preço do produto deve ter no máximo duas casas decimais")
    BigDecimal preco,
    
    boolean ativo,
    
    @Min(value = 0, message = "Estoque do produto não pode ser negativo")
    Integer estoque
) {
}