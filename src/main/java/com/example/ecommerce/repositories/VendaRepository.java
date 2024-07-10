package com.example.ecommerce.repositories;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.models.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
	List<Venda> findAllByDataBetween(Instant startDate, Instant endDate);
}
