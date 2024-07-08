package com.example.ecommerce.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProdutoMapper;
import com.example.ecommerce.mapper.VendaMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.repositories.ProdutoRepository;
import com.example.ecommerce.repositories.VendaRepository;

import jakarta.transaction.Transactional;

@Service
public class VendaServiceImpl implements VendaService {

	@Autowired
	private VendaRepository repository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private VendaMapper mapper;

	@Autowired
	private ProdutoMapper produtoMapper;

	@Override
	@Transactional
	public VendaDTO criar(VendaDTO vendaDTO) {
	    if (vendaDTO.produtos() == null || vendaDTO.produtos().isEmpty()) {
	        throw new IllegalArgumentException("Uma venda deve conter pelo menos um produto.");
	    }

	    List<Long> produtoIds = vendaDTO.produtos().stream().map(ProdutoDTO::id).collect(Collectors.toList());

	    List<Produto> produtos = produtoRepository.findAllById(produtoIds);

	    if (produtos.size() != vendaDTO.produtos().size()) {
	        throw new ResourceNotFoundException("Um ou mais produtos não foram encontrados");
	    }

	    Venda venda = mapper.toEntity(vendaDTO);

	    for (ProdutoDTO produtoDTO : vendaDTO.produtos()) {
            Produto produto = produtos.stream().filter(p -> p.getId().equals(produtoDTO.id())).findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + produtoDTO.id()));

            int quantidade = vendaDTO.produtosQuantidade().get(produtoDTO.id());
            venda.addProduto(produto, quantidade);
        }

	    venda.setData(Instant.now());
	    // TODO: calcular o total da venda

	    Venda salva = repository.save(venda);
	    return mapper.toDTO(salva);
	}


	@Override
	@Transactional
	public VendaDTO atualizar(Long id, VendaDTO vendaDTO) {
	    Venda venda = repository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com o ID: " + id));

	    // Carregar os produtos do banco de dados
	    Set<Produto> produtos = new HashSet<>();
	    for (ProdutoDTO produtoDTO : vendaDTO.produtos()) {
	        Produto produto = produtoRepository.findById(produtoDTO.id())
	                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoDTO.id()));
	        produtos.add(produto);
	    }

	    // Atualizar os dados da venda com base no DTO
	    mapper.updateFromDTO(vendaDTO, venda);

	    // Atualizar a coleção de produtos na venda
	    venda.getProdutos().clear();
	    venda.getProdutos().addAll(produtos);

	    // Salvar a venda atualizada
	    Venda atualizada = repository.save(venda);

	    return mapper.toDTO(atualizada);
	}


	@Override
	@Transactional
	public void deletar(Long id) {
		Venda venda = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com o ID: " + id));
		repository.delete(venda);
	}

	@Override
	public VendaDTO obterPorId(Long id) {
		Venda venda = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
		return mapper.toDTO(venda);
	}

	@Override
	public List<VendaDTO> listar() {
		List<Venda> vendas = repository.findAll();
		return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
	}

}
