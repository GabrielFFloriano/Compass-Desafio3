package com.example.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.models.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoDTO toDTO(Produto produto);
    Produto toEntity(ProdutoDTO produtoDTO);
    void updateFromDTO(ProdutoDTO produtoDTO, @MappingTarget Produto produto);
}