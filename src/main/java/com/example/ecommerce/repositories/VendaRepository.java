package com.example.ecommerce.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.models.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
	List<Venda> findAllByDataBetween(Instant startDate, Instant endDate);
	List<Venda> findByProdutosId(Long produtoId);

    @EntityGraph(attributePaths = {"produtos"})
	@NonNull
    Optional<Venda> findById(@Param("id") @NonNull Long id);

    @EntityGraph(attributePaths = {"produtos"})
	@NonNull
    List<Venda> findAll();
}
