package com.estoque.demo_10.service;

import com.estoque.demo_10.model.Produto;
import com.estoque.demo_10.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE - Camada de lógica de negócio
 *
 * FUNÇÃO: Contém a regra de negócio da aplicação
 * ANOTAÇÃO: @Service - Marca como componente Spring de serviço
 *
 * RESPONSABILIDADES:
 * 1. Orquestrar operações entre Repository e Controller
 * 2. Aplicar regras de negócio e validações
 * 3. Gerenciar transações (@Transactional)
 * 4. Converter DTOs ↔ Entidades
 *
 * PRINCÍPIO: Controller não acessa Repository diretamente!
 * Toda comunicação passa pelo Service.
 */
@Service  // ← Componente Spring para lógica de negócio
public class ProdutoService {

    /**
     * INJEÇÃO DE DEPENDÊNCIA - O Spring fornece automaticamente
     *
     * @Autowired: Spring procura uma implementação de ProdutoRepository
     * e injeta aqui (padrão Singleton - mesma instância para toda app)
     */
    @Autowired
    private ProdutoRepository produtoRepository;

    // =============== OPERAÇÕES CRUD ===============

    /**
     * LISTAR TODOS - Apenas delega para o Repository
     *
     * @return Lista de todos os produtos
     */
    public List<Produto> listarTodos() {
        // Simples delegação - sem regras de negócio
        return produtoRepository.findAll();
    }

    /**
     * BUSCAR POR ID - Com tratamento de Optional
     *
     * @param id ID do produto
     * @return Optional (pode estar vazio se não encontrar)
     */
    public Optional<Produto> buscarPorId(Long id) {
        // Optional evita NullPointerException
        return produtoRepository.findById(id);
    }

    /**
     * SALVAR PRODUTO - Com validação de negócio
     *
     * @Transactional: Todas as operações dentro do método
     * são uma única transação no banco
     *
     * @param produto Produto a ser salvo
     * @return Produto salvo (com ID preenchido)
     */
    @Transactional  // ← Transação automática: commit se sucesso, rollback se erro
    public Produto salvar(Produto produto) {
        // REGRA DE NEGÓCIO: Preço não pode ser negativo
        if (produto.getPreco().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        // REGRA DE NEGÓCIO: Quantidade mínima
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }

        // Se não tem ID, é um novo produto → INSERT
        // Se tem ID, é atualização → UPDATE
        return produtoRepository.save(produto);
    }

    /**
     * DELETAR PRODUTO
     *
     * @param id ID do produto a deletar
     */
    @Transactional
    public void deletar(Long id) {
        // Verifica se existe antes de deletar
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    /**
     * BUSCAR POR NOME - Com tratamento de caso vazio
     *
     * @param nome (ou parte do nome) para buscar
     * @return Lista de produtos encontrados
     */
    public List<Produto> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            // Se nome vazio, retorna todos
            return listarTodos();
        }
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * EXEMPLO: MÉTODO COM LÓGICA DE NEGÓCIO COMPLEXA
     *
     * Atualiza estoque após uma venda
     *
     * @param idProduto ID do produto
     * @param quantidadeVendida Quantidade vendida
     * @return Produto atualizado
     */
    @Transactional
    public Produto registrarVenda(Long idProduto, Integer quantidadeVendida) {
        // 1. Busca produto
        Produto produto = buscarPorId(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 2. Valida se tem estoque suficiente
        if (produto.getQuantidade() < quantidadeVendida) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " +
                    produto.getQuantidade());
        }

        // 3. Atualiza estoque
        produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);

        // 4. Salva alterações
        return produtoRepository.save(produto);

        // @Transactional garante que se algo falhar, nada é salvo
    }
}