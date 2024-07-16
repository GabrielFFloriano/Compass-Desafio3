package com.example.ecommerce.mapper;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.ProdutoVenda;
import com.example.ecommerce.models.Venda;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VendaMapper {

    @Mapping(target = "produtos", source = "produtos", qualifiedByName = "mapProdutoVendaSetToProdutoDTOSet")
    @Mapping(target = "produtosQuantidade", source = "produtos", qualifiedByName = "mapProdutoVendaSetToQuantidadeMap")
    VendaDTO toDTO(Venda venda);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "produtos", source = "produtos", qualifiedByName = "mapProdutoDTOSetToProdutoVendaSet")
    Venda toEntity(VendaDTO vendaDTO);

    @Mapping(target = "produtos", ignore = true)
    void updateFromDTO(VendaDTO vendaDTO, @MappingTarget Venda venda);

    @Named("mapProdutoVendaToProdutoDTO")
    default ProdutoDTO mapProdutoVendaToProdutoDTO(ProdutoVenda produtoVenda) {
        Produto produto = produtoVenda.getProduto();
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.isAtivo(),
            produto.getEstoque(),
            null
        );
    }

    @Named("mapProdutoVendaSetToProdutoDTOSet")
    default Set<ProdutoDTO> mapProdutoVendaSetToProdutoDTOSet(Set<ProdutoVenda> produtoVendaSet) {
        return produtoVendaSet.stream()
                .map(this::mapProdutoVendaToProdutoDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapProdutoDTOToProdutoVenda")
    default ProdutoVenda mapProdutoDTOToProdutoVenda(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setId(produtoDTO.id());
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setAtivo(produtoDTO.ativo());
        produto.setEstoque(produtoDTO.estoque());

        ProdutoVenda produtoVenda = new ProdutoVenda();
        produtoVenda.setProduto(produto);
        produtoVenda.setQuantidade(1); // Ajuste conforme necessário

        return produtoVenda;
    }

    @Named("mapProdutoDTOSetToProdutoVendaSet")
    default Set<ProdutoVenda> mapProdutoDTOSetToProdutoVendaSet(Set<ProdutoDTO> produtoDTOSet) {
        return produtoDTOSet.stream()
                .map(this::mapProdutoDTOToProdutoVenda)
                .collect(Collectors.toSet());
    }
    
    @Named("mapProdutoVendaSetToQuantidadeMap")
    default Map<Long, Integer> mapProdutoVendaSetToQuantidadeMap(Set<ProdutoVenda> produtoVendaSet) {
        return produtoVendaSet.stream()
                .collect(Collectors.toMap(pv -> pv.getProduto().getId(), ProdutoVenda::getQuantidade));
    }
}
