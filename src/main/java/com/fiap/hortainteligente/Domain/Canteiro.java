package com.fiap.hortainteligente.Domain;

//regra de negocios
//fica todas as entidades do meu negocio


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//marca a classe como uma
@Entity
@Table(name = "TB_CANTEIRO") //Defini o nome da tabela
@Getter //gera o get
@Setter //gera o set
public class Canteiro {

    @Id //defini a chave prmaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //defini como a chave primaria é gerada,nesse caso o proprio banco de dados tem a resposnsabilida
    @Column(name = "ID_CANTEIRO") //nome da colna
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "LARGURA_METROS", nullable = false) //not null
    private double larguraMetros;

    @Column(name = "COMPRIMENTO_METROS" , nullable = false)
    private double comprimentoMetros;

    @Column(name = "UMIDADE_ATUAL_PERCENTUAL", nullable = false)
    private int umidadeAtualPercentual;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CULTURA" ,nullable = false, length = 30)
    private TipoCultura tipoCultura;
}
