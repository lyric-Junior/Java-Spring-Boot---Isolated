package com.estoque.demo_10.model;

/* Aqui temos três bibliotecas super importantes para o desenvolvimento de um model para essa API.
 * BigDecimal = Usada para matemática utilizando dinheiro, já que utilizar float definitivamente seria inviável
 * persistence = O persistence adiciona comandos e anotações ao spring como o @Entity que representa uma table no mysql.
 * @Entity @Id @GeneratedValue @Column
 *
 */
import java.math.BigDecimal;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
public class Produto {
    /* Aqui temos um exemplo bom de como o persistence funciona
    * Observe que o @Id é um comando de generatedValue, ou seja gerado automaticamente.*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    // @Column criamos uma coluna aonde iremos inserir os valores que q
    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    //Contrutores
    public Produto() {
        this.dataCadastro = LocalDateTime.now();
    }

    //Vamos criar um construtor extra que aparentemente é útil para criar objetos já preenchidos
    public Produto(String nome, String descricao, BigDecimal preco, Integer quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    //getters e setters, vão enviar e retornar os valores de cada dado em tempo real puxando valores do banco de dados
    public Long getId() {return Id;}
    public void setId(Long Id) {this.Id = Id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public BigDecimal getPreco() {return preco;}
    public void setPreco(BigDecimal preco) {this.preco = preco;}

    public Integer getQuantidade() {return quantidade;}

    public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}

    public LocalDateTime getDataCadastro() {return dataCadastro;}

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return String.format(
                "Produto[Id=%d, nome='%s', preco=%.2f, quantidade=%d]",
                Id, nome, preco, quantidade
        );
    }
}