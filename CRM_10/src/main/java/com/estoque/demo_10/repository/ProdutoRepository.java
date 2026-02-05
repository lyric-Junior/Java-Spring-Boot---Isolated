package com.estoque.demo_10.repository;

/* ============================================================================
* As bibliotecas utilizadas:
* Produto: Importamos todas as informações da Entity que iremos utilizar.
* =============================================================================
* JpaRepository:
* Não existe no java puro, é um framework que gerencia banco de dados sem escrever códigos imensos, assim adicionamos comandos como por exemplo, save(Produto), findById(), findAll, delete(), deleteById(), existsById(), count().
*///===========================================================================

import com.estoque.demo_10.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//=======================================================
// Listas em Java.
import java.util.List;
//=======================================================

//Repository será a interface de gerenciamento do banco de dados em sql, no caso essa anotação justamente serve para que o spring entenda que você

@Repository
public interface ProdutoRepository
    extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /*
* OUTROS EXEMPLOS DE MÉTODOS AUTOMÁTICOS:
            */
    // List<Produto> findByPrecoGreaterThan(BigDecimal preco);
    // List<Produto> findByQuantidadeLessThan(Integer quantidade);
    // List<Produto> findByNomeStartingWith(String prefixo);
    // List<Produto> findByDataCadastroAfter(LocalDateTime data);
    // Produto findFirstByOrderByPrecoDesc(); // Produto mais caro

    /**
     * Para queries complexas, usa-se @Query:
     */
    /*
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :min AND :max")
    List<Produto> buscarPorFaixaDePreco(
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max
    );
    */
}

