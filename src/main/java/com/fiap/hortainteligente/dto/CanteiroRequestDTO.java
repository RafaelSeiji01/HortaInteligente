package com.fiap.hortainteligente.dto;

//aqui chaga o json para cadastrar um canteiro

import com.fiap.hortainteligente.Domain.TipoCultura;
import jakarta.validation.constraints.*;

//utilizando o record, todos os atributos sao private final por padrao
//segurança, impede alterações

public record CanteiroRequestDTO(

        @NotBlank(message = "o nome do canteiro é obrigatorio") //verifica se o campo nao é nulo
        String nome,

        @Positive(message = "A largura deve superior a zero")//o numero deve ser maior que 0
        double larguraMetros,

        @Positive(message = "o comprimento deve ser maior de zero")
        double comprimentosMetros,

        @Min(value = 0, message = "a unidade minima é de 0%") //minimo
        @Max(value = 100, message = "a unidade maxima é de 100%") //maximo
        int umidadeAtualPercentual,

        @NotNull(message = "O tipo de cultura deve ser informado (HORTALICA, TUBERCULO, GRAO).")
        TipoCultura tipoCultura

) {
}
