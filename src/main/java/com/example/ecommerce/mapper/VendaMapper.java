package com.example.ecommerce.mapper;

import org.mapstruct.Mapper;

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.models.Venda;

@Mapper(componentModel = "spring")
public interface VendaMapper {
    VendaDTO toDTO(Venda venda);
    Venda toEntity(VendaDTO vendaDTO);
}