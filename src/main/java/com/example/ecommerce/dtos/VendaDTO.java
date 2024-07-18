package com.example.ecommerce.dtos;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;

public record VendaDTO(
    Long id,
    
    Instant data,
  
    Set<ProdutoDTO> produtos,
    
    Map<Long, Integer> produtosQuantidade,
    
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    BigDecimal total,
    
    Long usuarioId 
    
) {
}