package com.fiap.hortainteligente.Mapper;

//responsabilidade dessa classe:
//convverte o dto de request para entidade carteiro(para guardar na base de dados)
//converte a entidade carterio para o response DTO(devolve json)

import com.fiap.hortainteligente.Domain.Canteiro;
import com.fiap.hortainteligente.dto.CanteiroRequestDTO;
import com.fiap.hortainteligente.dto.CanteiroResponseDTO;

public class CanteiroMapper {

    // Converte DTO recebido da web na Entidade do banco
    public static Canteiro toEntity(CanteiroRequestDTO dto){
        Canteiro canteiro = new Canteiro();
        canteiro.setNome(dto.nome());
        canteiro.setLarguraMetros(dto.larguraMetros());
        canteiro.setComprimentoMetros(dto.comprimentosMetros());
        canteiro.setUmidadeAtualPercentual(dto.umidadeAtualPercentual());
        canteiro.setTipoCultura(dto.tipoCultura());
        return canteiro;
    }

    // Converte a Entidade salva + cálculos no DTO de resposta (JSON)
    public static CanteiroResponseDTO toResposeDTO(Canteiro entity, double areaTotal, boolean precisaIrrigar){
        return new CanteiroResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getComprimentoMetros(),
                entity.getLarguraMetros(),
                areaTotal,
                entity.getUmidadeAtualPercentual(),
                entity.getTipoCultura(),
                precisaIrrigar
        );
    }
}
