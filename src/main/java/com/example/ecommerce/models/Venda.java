package com.example.ecommerce.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Venda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private Instant data = Instant.now();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "ProdutoVenda", joinColumns = @JoinColumn(name = "idVenda"), inverseJoinColumns = @JoinColumn(name = "idProduto"))
	private Set<Produto> produtos = new HashSet<>();

	@JdbcTypeCode(SqlTypes.JSON)
	private Map<Long, Integer> produtosQuantidade;

	@Column(nullable = false)
	private BigDecimal total;

	public Long getId() {
		return id;
	}

	public Instant getData() {
		return data;
	}

	public void setData(Instant data) {
		this.data = data;
	}

	public Set<Produto> getProdutos() {
		return produtos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void addProduto(Produto produto, Integer quantidade) {
		if (produto == null) {
			throw new IllegalArgumentException("Produto não pode ser nulo.");
		}
		if (quantidade == null || quantidade <= 0) {
			throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
		}

		if (produtosQuantidade.containsKey(produto)) {
			produtosQuantidade.put(produto.getId(), quantidade);
		} else {
			produtosQuantidade.put(produto.getId(), quantidade);
			produtos.add(produto); 
			produto.getVendas().add(this);
		}
	}
}