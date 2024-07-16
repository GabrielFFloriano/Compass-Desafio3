package com.example.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.models.Usuario;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

	UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "vendas", source = "vendas", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    @Mapping(target = "senha", ignore = true)
	void updateFromDTO(UsuarioDTO usuarioDTO, @MappingTarget Usuario usuario);
}
