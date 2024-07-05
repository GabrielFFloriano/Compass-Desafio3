package com.example.ecommerce.dtos;
import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;

public record VendaDTO(
    Long id,
    
    Instant data,
  
    @DecimalMin(value = "0.01", message = "Preço do produto deve ser positivo")
    BigDecimal total
    
) {
}