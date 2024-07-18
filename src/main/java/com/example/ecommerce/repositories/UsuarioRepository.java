package com.example.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(String email);

	@EntityGraph(attributePaths = { "vendas", "vendas.produtos" })
	@NonNull
	Optional<Usuario> findById(@NonNull Long id);
	
	@EntityGraph(attributePaths = {"vendas", "vendas.produtos"})
	@NonNull
    List<Usuario> findAll();
}
