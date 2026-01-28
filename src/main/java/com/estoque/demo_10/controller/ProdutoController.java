package com.estoque.demo_10.controller;

import com.estoque.demo_10.model.Produto;
import com.estoque.demo_10.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER - Camada que lida com requisições HTTP
 *
 * FUNÇÃO: Receber requisições web, processar e retornar respostas
 * ANOTAÇÃO: @Controller - Para aplicações web com templates
 *          @RestController - Para APIs REST (retorna JSON)
 *
 * RESPONSABILIDADES:
 * 1. Mapear URLs para métodos (roteamento)
 * 2. Extrair dados da requisição (parâmetros, corpo JSON, etc.)
 * 3. Chamar Service para processar
 * 4. Preparar resposta (HTML, JSON, redirecionamento)
 *
 * NUNCA deve conter lógica de negócio! Apenas orquestração.
 */
@Controller  // ← Para aplicações web com Thymeleaf/HTML
@RequestMapping("/produtos")  // ← Todas as URLs começam com /produtos
public class ProdutoController {

    /**
     * INJEÇÃO DO SERVICE - Controller depende do Service
     */
    @Autowired
    private ProdutoService produtoService;

    // =============== MÉTODOS QUE RETORNAM HTML (Thymeleaf) ===============

    /**
     * LISTAR PRODUTOS - Página principal
     *
     * @GetMapping: Mapeia requisições GET para /produtos
     *
     * @param model Container para dados enviados ao template
     * @return Nome do template HTML (produtos/listar.html)
     */
    @GetMapping  // ← Equivalente a @RequestMapping(method = RequestMethod.GET)
    public String listarProdutos(Model model) {
        // 1. Obtém dados do Service
        List<Produto> produtos = produtoService.listarTodos();

        // 2. Adiciona ao Model (será acessível no template)
        model.addAttribute("produtos", produtos);
        // No template: ${produtos} para acessar a lista

        // 3. Retorna nome do template (sem extensão .html)
        return "produtos/listar";  // ← src/main/resources/templates/produtos/listar.html
    }

    /**
     * FORMULÁRIO DE NOVO PRODUTO
     *
     * @param model
     * @return Template do formulário
     */
    @GetMapping("/novo")  // ← GET /produtos/novo
    public String novoProduto(Model model) {
        // Cria um produto vazio para o formulário
        model.addAttribute("produto", new Produto());
        // No template: th:object="${produto}" para vincular

        return "produtos/formulario";
    }

    /**
     * SALVAR PRODUTO (CREATE ou UPDATE)
     *
     * @PostMapping: Mapeia requisições POST
     *
     * @ModelAttribute: Vincula dados do formulário ao objeto Produto
     * Spring automaticamente preenche: produto.setNome(request.getParameter("nome"))
     *
     * @param produto Objeto preenchido com dados do formulário
     * @return Redireciona para lista de produtos
     */
    @PostMapping("/salvar")  // ← POST /produtos/salvar
    public String salvarProduto(@ModelAttribute Produto produto) {
        // Delega para o Service (que aplica regras de negócio)
        produtoService.salvar(produto);

        // Redireciona para evitar reenvio do formulário (Post/Redirect/Get)
        return "redirect:/produtos";  // ← Faz nova requisição GET para /produtos
    }

    /**
     * FORMULÁRIO DE EDIÇÃO
     *
     * @PathVariable: Extrai {id} da URL /produtos/editar/{id}
     *
     * @param id ID do produto (da URL)
     * @param model
     * @return Template preenchido com dados do produto
     */
    @GetMapping("/editar/{id}")  // ← GET /produtos/editar/1
    public String editarProduto(@PathVariable Long id, Model model) {
        // Busca produto pelo ID
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "produtos/formulario";  // Mesmo template, mas preenchido
    }

    /**
     * EXCLUIR PRODUTO
     */
    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/produtos";
    }

    // =============== EXEMPLO: MÉTODO PARA API REST (JSON) ===============
    /**
     * Se quiser uma API REST também, pode adicionar:
     */
    /*
    @RestController  // ← Em vez de @Controller
    @RequestMapping("/api/produtos")  // ← Prefixo diferente para API
    public class ProdutoApiController {

        @GetMapping
        public List<Produto> listar() {
            return produtoService.listarTodos();
            // Spring converte automaticamente para JSON
            // Retorna: [{"id":1,"nome":"Produto1"},...]
        }

        @GetMapping("/{id}")
        public ResponseEntity<Produto> buscar(@PathVariable Long id) {
            return produtoService.buscarPorId(id)
                .map(produto -> ResponseEntity.ok(produto))
                .orElse(ResponseEntity.notFound().build());
        }
    }
    */
}