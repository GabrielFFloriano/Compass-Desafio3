package com.example.ecommerce.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mapper.VendaMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.ProdutoVenda;
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

    @Override
    @Transactional
    @CacheEvict(value = "vendasCache", allEntries = true)
    public VendaDTO criar(VendaDTO vendaDTO) {
        if (vendaDTO.produtos() == null || vendaDTO.produtos().isEmpty()) {
            throw new IllegalArgumentException("Uma venda deve conter pelo menos um produto.");
        }

        List<Long> produtoIds = vendaDTO.produtos().stream().map(ProdutoDTO::id).collect(Collectors.toList());
        List<Produto> produtos = produtoRepository.findAllById(produtoIds);

        if (produtos.size() != vendaDTO.produtos().size()) {
            throw new ResourceNotFoundException("Um ou mais produtos não foram encontrados");
        }

        Venda venda = new Venda();
        venda.setData(Instant.now());
        BigDecimal total = BigDecimal.ZERO;

        for (ProdutoDTO produtoDTO : vendaDTO.produtos()) {
            Produto produto = produtos.stream().filter(p -> p.getId().equals(produtoDTO.id())).findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + produtoDTO.id()));

            if (vendaDTO.produtosQuantidade() == null) {
                throw new IllegalArgumentException("A quantidade dos produtos não pode ser nula.");
            }

            Integer quantidade = vendaDTO.produtosQuantidade().get(produtoDTO.id());

            if (quantidade == null) {
                throw new IllegalArgumentException("A quantidade do produto " + produtoDTO.id() + " não pode ser nula.");
            }

            if (produto.getEstoque() < quantidade) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            produto.setEstoque(produto.getEstoque() - quantidade);
            produtoRepository.save(produto);
            
            venda.addProduto(produto, quantidade);

            BigDecimal subTotal = produto.isAtivo() ? produto.getPreco().multiply(BigDecimal.valueOf(quantidade)) : BigDecimal.ZERO;
            total = total.add(subTotal);
        }
        venda.setTotal(total);
        Venda salva = repository.save(venda);
        return mapper.toDTO(salva);
    }


    @Override
    @Transactional
    @CacheEvict(value = "vendasCache", allEntries = true)
    public VendaDTO atualizar(Long id, VendaDTO vendaDTO) {
        Venda venda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com o ID: " + id));

        if (vendaDTO.produtosQuantidade() != null) {
            List<Long> produtoIds = vendaDTO.produtosQuantidade().keySet().stream().collect(Collectors.toList());
            List<Produto> produtos = produtoRepository.findAllById(produtoIds);

            for (Produto produto : produtos) {
                if (!produto.isAtivo()) {
                    throw new IllegalStateException("Produto inativo não pode ser utilizado na venda: " + produto.getNome());
                }
            }
        }
        
        if (vendaDTO.data() != null) {
            venda.setData(vendaDTO.data());
        }

        if (vendaDTO.produtosQuantidade() != null) {
            Map<Long, ProdutoVenda> produtoVendaMap = new HashMap<>();
            venda.getProdutos()
                    .forEach(produtoVenda -> produtoVendaMap.put(produtoVenda.getProduto().getId(), produtoVenda));

            for (Map.Entry<Long, Integer> entry : vendaDTO.produtosQuantidade().entrySet()) {
                Long produtoId = entry.getKey();
                Integer quantidade = entry.getValue();

                ProdutoVenda produtoVenda = produtoVendaMap.get(produtoId);
                if (produtoVenda != null) {
                    produtoVenda.setQuantidade(quantidade);
                } else {
                    Produto produto = produtoRepository.findById(produtoId).orElseThrow(
                            () -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId));

                    produtoVenda = new ProdutoVenda();
                    produtoVenda.setVenda(venda);
                    produtoVenda.setProduto(produto);
                    produtoVenda.setQuantidade(quantidade);

                    venda.getProdutos().add(produtoVenda);
                }
            }

            venda.getProdutos().removeIf(
                    produtoVenda -> !vendaDTO.produtosQuantidade().containsKey(produtoVenda.getProduto().getId()));
        }

        BigDecimal total = calcularTotal(venda);
        venda.setTotal(total);

        Venda atualizada = repository.save(venda);
        return mapper.toDTO(atualizada);
    }

    private BigDecimal calcularTotal(Venda venda) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProdutoVenda produtoVenda : venda.getProdutos()) {
            BigDecimal subtotal = produtoVenda.getProduto().getPreco()
                    .multiply(BigDecimal.valueOf(produtoVenda.getQuantidade()));
            total = total.add(subtotal);
        }
        return total;
    }

    @Override
    @Transactional
    @CacheEvict(value = "vendasCache", allEntries = true)
    public void deletar(Long id) {
        Venda venda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com o ID: " + id));
        for (ProdutoVenda produtoVenda : venda.getProdutos()) {
            Produto produto = produtoVenda.getProduto();
            int quantidadeVendida = produtoVenda.getQuantidade();
            produto.setEstoque(produto.getEstoque() + quantidadeVendida);
            produto.getVendas().remove(produtoVenda);
            produtoRepository.save(produto);
        }
        venda.getProdutos().clear();
        repository.delete(venda);
    }

    @Override
    @Transactional
    @Cacheable(value = "vendasCache")
    public VendaDTO obterPorId(Long id) {
        Venda venda = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        return mapper.toDTO(venda);
    }

    @Override
    @Transactional
    @Cacheable(value = "vendasCache")
    public List<VendaDTO> listar() {
        List<Venda> vendas = repository.findAll();
        return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "vendasCache", key = "#startDate.toString() + '_' + #endDate.toString()")
    public List<VendaDTO> filtrarVendasPorData(Instant startDate, Instant endDate) {
        List<Venda> vendas = repository.findAllByDataBetween(startDate, endDate);
        return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "vendasCache")
    public List<VendaDTO> gerarRelatorioSemanal() {
        Instant startOfWeek = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toInstant(ZoneOffset.UTC);
        Instant endOfWeek = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toInstant(ZoneOffset.UTC);

        List<Venda> vendas = repository.findAllByDataBetween(startOfWeek, endOfWeek);
        return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "vendasCache")
    public List<VendaDTO> gerarRelatorioMensal() {
        Instant startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).toInstant(ZoneOffset.UTC);
        Instant endOfMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).toInstant(ZoneOffset.UTC);

        List<Venda> vendas = repository.findAllByDataBetween(startOfMonth, endOfMonth);
        return vendas.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

}
