package com.example.ecommerce.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ecommerce.dtos.ProdutoDTO;
import com.example.ecommerce.dtos.VendaDTO;
import com.example.ecommerce.mapper.VendaMapper;
import com.example.ecommerce.models.Produto;
import com.example.ecommerce.models.Venda;
import com.example.ecommerce.repositories.ProdutoRepository;
import com.example.ecommerce.repositories.VendaRepository;
import com.example.ecommerce.services.VendaServiceImpl;

@SpringBootTest
public class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private VendaMapper mapper;

    @InjectMocks
    private VendaServiceImpl vendaService;

    private Venda venda1;
    private Venda venda2;
    private VendaDTO vendaDTO1;
    private VendaDTO vendaDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        venda1 = new Venda();
        venda1.setId(1L);
        venda1.setData(Instant.parse("2023-07-01T00:00:00Z"));

        venda2 = new Venda();
        venda2.setId(2L);
        venda2.setData(Instant.parse("2023-07-02T00:00:00Z"));

        vendaDTO1 = new VendaDTO(1L, Instant.parse("2023-07-01T00:00:00Z"), null, null, null);
        vendaDTO2 = new VendaDTO(2L, Instant.parse("2023-07-02T00:00:00Z"), null, null, null);
    }

    @Test
    void testCriarVenda() {
        // Criando um ProdutoDTO simulado
        ProdutoDTO produtoDTO = new ProdutoDTO(1L, "Produto A", "Descrição do Produto A", BigDecimal.valueOf(100.0), true, 10);

        // Criando um mapa de produtos e quantidades simulado
        Map<Long, Integer> produtosQuantidade = new HashMap<>();
        produtosQuantidade.put(1L, 2);

        // Criando um VendaDTO simulado
        VendaDTO vendaDTO = new VendaDTO(null, Instant.now(), Set.of(produtoDTO), produtosQuantidade, BigDecimal.valueOf(200.0));
        
        // Configurando o comportamento esperado do mock do produtoRepository
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto A");
        produto.setDescricao("Descrição do Produto A");
        produto.setPreco(BigDecimal.valueOf(100.0));
        produto.setEstoque(10);

        // Configurando o mock do produtoRepository para retornar o produto quando findAllById é chamado
        when(produtoRepository.findAllById(any())).thenReturn(List.of(produto));
        // Configurando o mock do mapper para retornar um objeto Venda vazio ao receber qualquer VendaDTO
        when(mapper.toEntity(any(VendaDTO.class))).thenReturn(new Venda());
        when(mapper.toDTO(any(Venda.class))).thenReturn(vendaDTO);

        // Configurando o mock do vendaRepository para retornar o objeto Venda recebido ao chamar save
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Chamando o método do serviço para criar a venda
        VendaDTO result = vendaService.criar(vendaDTO);

        // Verificando se o resultado não é nulo
        assertNotNull(result);
        // Verificando se o resultado é igual ao DTO esperado
        assertEquals(vendaDTO, result);
        // Verificando se o método save do repository foi chamado uma vez
        verify(vendaRepository, times(1)).save(any(Venda.class));

        // Verificando se o método findAllById do produtoRepository foi chamado com o ID correto
        verify(produtoRepository, times(1)).findAllById(any());
    }

    @Test
    void testAtualizarVenda() {
        // Configurar um cenário de venda existente
        Long vendaId = 1L;
        Venda vendaExistente = new Venda();
        vendaExistente.setId(vendaId);
    
        // Criar um Produto simulado
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto A");
        produto.setDescricao("Descrição do Produto A");
        produto.setPreco(BigDecimal.valueOf(100.0));
        produto.setEstoque(10);
    
        // Criar um ProdutoDTO simulado
        ProdutoDTO produtoDTO = new ProdutoDTO(1L, "Produto A", "Descrição do Produto A", BigDecimal.valueOf(100.0), true, 10);
    
        // Criar um mapa de produtos e quantidades simulado
        Map<Long, Integer> produtosQuantidade = new HashMap<>();
        produtosQuantidade.put(1L, 2);
    
        // Criar um VendaDTO simulado com novos dados
        VendaDTO vendaDTOAtualizada = new VendaDTO(vendaId, Instant.now(), Set.of(produtoDTO), produtosQuantidade, BigDecimal.valueOf(200.0));
    
        // Configurar o comportamento do mock do vendaRepository para retornar a venda existente ao buscar por ID
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(vendaExistente));
    
        // Configurar comportamento esperado do mock do produtoRepository para encontrar produtos
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));
    
        // Configurar o mock do mapper para chamar o método updateFromDTO sem definir um comportamento específico
        doNothing().when(mapper).updateFromDTO(vendaDTOAtualizada, vendaExistente);
    
        // Configurar o mock do vendaRepository para retornar a venda atualizada ao salvar
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaExistente);
    
        // Chamar o método do serviço para atualizar a venda
        VendaDTO result = vendaService.atualizar(vendaId, vendaDTOAtualizada);
    
        // Verificar se o resultado não é nulo
        assertNotNull(result);
    
        // Verificar se o ID da venda atualizada é igual ao ID esperado
        assertEquals(vendaId, result.id());
    
        // Verificar se o método save do repository foi chamado uma vez
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void testFiltrarVendasPorData() {
        Instant startDate = Instant.parse("2023-07-01T00:00:00Z");
        Instant endDate = Instant.parse("2023-07-31T23:59:59Z");

        when(vendaRepository.findAllByDataBetween(startDate, endDate)).thenReturn(List.of(venda1, venda2));
        when(mapper.toDTO(venda1)).thenReturn(vendaDTO1);
        when(mapper.toDTO(venda2)).thenReturn(vendaDTO2);

        List<VendaDTO> vendas = vendaService.filtrarVendasPorData(startDate, endDate);
        assertEquals(2, vendas.size());
        assertEquals(1L, vendas.get(0).id());
        assertEquals(2L, vendas.get(1).id());

        verify(vendaRepository).findAllByDataBetween(startDate, endDate);
    }

    @Test
void testGerarRelatorioSemanal() {
    // Definir o início e o fim da semana atual em Instant
    Instant startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                      .atStartOfDay()
                                      .toInstant(ZoneOffset.UTC);
    Instant endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                                    .atTime(LocalTime.MAX)
                                    .toInstant(ZoneOffset.UTC);

    // Simular vendas retornadas pelo repository para o período da semana
    when(vendaRepository.findAllByDataBetween(startOfWeek, endOfWeek))
            .thenReturn(List.of(venda1, venda2));

    // Mapear as vendas para DTOs simulados
    when(mapper.toDTO(venda1)).thenReturn(vendaDTO1);
    when(mapper.toDTO(venda2)).thenReturn(vendaDTO2);

    // Chamar o método do serviço para gerar o relatório semanal
    List<VendaDTO> vendas = vendaService.gerarRelatorioSemanal();

    // Verificar se o número de vendas no relatório está correto
    assertEquals(2, vendas.size());
    // Verificar os IDs das vendas no relatório
    assertEquals(1L, vendas.get(0).id());
    assertEquals(2L, vendas.get(1).id());

    // Verificar se o método findAllByDataBetween do repository foi chamado corretamente
    verify(vendaRepository).findAllByDataBetween(startOfWeek, endOfWeek);
}


@Test
void testGerarRelatorioMensal() {
    // Definir o início e o fim do mês atual em Instant
    Instant startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
                                         .atStartOfDay()
                                         .toInstant(ZoneOffset.UTC);
    Instant endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
                                       .atTime(LocalTime.MAX)
                                       .toInstant(ZoneOffset.UTC);

    // Simular vendas retornadas pelo repository para o período do mês
    when(vendaRepository.findAllByDataBetween(startOfMonth, endOfMonth))
            .thenReturn(List.of(venda1, venda2));

    // Mapear as vendas para DTOs simulados
    when(mapper.toDTO(venda1)).thenReturn(vendaDTO1);
    when(mapper.toDTO(venda2)).thenReturn(vendaDTO2);

    // Chamar o método do serviço para gerar o relatório mensal
    List<VendaDTO> vendas = vendaService.gerarRelatorioMensal();

    // Verificar se o número de vendas no relatório está correto
    assertEquals(2, vendas.size());
    // Verificar os IDs das vendas no relatório
    assertEquals(1L, vendas.get(0).id());
    assertEquals(2L, vendas.get(1).id());

    // Verificar se o método findAllByDataBetween do repository foi chamado corretamente
    verify(vendaRepository).findAllByDataBetween(startOfMonth, endOfMonth);
}

}
