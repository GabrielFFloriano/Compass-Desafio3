package com.example.ecommerce.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.models.Venda;

@Mapper(componentModel = "spring")
public interface VendaMapper {

    @Mapping(target = "produtos", source = "produtos")
    VendaDTO toDTO(Venda venda);

    @Mapping(target = "id", ignore = true)
    Venda toEntity(VendaDTO vendaDTO);

	void updateFromDTO(VendaDTO vendaDTO, @MappingTarget Venda venda);

}