package com.example.ecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.models.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
}
