package com.example.ecommerce.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.ecommerce.dtos.UsuarioDTO;
import com.example.ecommerce.models.Usuario;
import com.example.ecommerce.models.Venda;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
		ProdutoMapper.class, VendaMapper.class })
public interface UsuarioMapper {

	@Mapping(target = "vendasIds", source = "vendas", qualifiedByName = "mapVendasToIds")
	UsuarioDTO toDTO(Usuario usuario);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "senha", ignore = true)
	@Mapping(target = "vendas", source = "vendasIds", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	Usuario toEntity(UsuarioDTO usuarioDTO);

	@Mapping(target = "senha", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "vendas", source = "vendasIds", qualifiedByName = "mapVendasIdsToVendas")
	void updateFromDTO(UsuarioDTO usuarioDTO, @MappingTarget Usuario usuario);

	@Named("mapVendasToIds")
	default Set<Long> mapVendasToIds(Set<Venda> vendas) {
		if (vendas == null) {
			return null;
		}
		return vendas.stream().map(Venda::getId).collect(Collectors.toSet());
	}

	@Named("mapVendasIdsToVendas")
	default Set<Venda> mapVendasIdsToVendas(Set<Long> vendasIds) {
		if (vendasIds == null) {
			return null;
		}
		return vendasIds.stream().map(id -> {
			Venda venda = new Venda();
			venda.setId(id);
			return venda;
		}).collect(Collectors.toSet());
	}
}
