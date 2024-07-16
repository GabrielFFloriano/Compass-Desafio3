package com.example.ecommerce.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.ProdutoVenda;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProdutoMapper {

    @Mapping(target = "vendas", source = "vendas", qualifiedByName = "toProdutoVendas")
    Produto toEntity(ProdutoDTO produtoDTO);

    @Mapping(target = "vendas", source = "vendas", qualifiedByName = "toVendasIds")
    ProdutoDTO toDTO(Produto produto);

    @Mapping(target = "vendas", ignore = true)
    void updateFromDTO(ProdutoDTO produtoDTO, @MappingTarget Produto produto);

    @Named("toProdutoVendas")
    default Set<ProdutoVenda> toProdutoVendas(Set<Long> vendasIds) {
        if (vendasIds == null) {
            return null;
        }
        return vendasIds.stream()
                .map(vendaId -> {
                    ProdutoVenda produtoVenda = new ProdutoVenda();
                    produtoVenda.setId(vendaId);
                    return produtoVenda;
                })
                .collect(Collectors.toSet());
    }

    @Named("toVendasIds")
    default Set<Long> toVendasIds(Set<ProdutoVenda> produtoVendas) {
        if (produtoVendas == null) {
            return null;
        }
        return produtoVendas.stream()
                .map(ProdutoVenda::getId)
                .collect(Collectors.toSet());
    }
}
