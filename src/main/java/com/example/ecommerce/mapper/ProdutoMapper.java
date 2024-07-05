package com.example.ecommerce.mapper;

import org.mapstruct.Mapper;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.models.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoDTO toDTO(Produto produto);
    Produto toEntity(ProdutoDTO produtoDTO);
}