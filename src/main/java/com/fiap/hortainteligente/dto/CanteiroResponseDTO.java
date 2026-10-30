package com.fiap.hortainteligente.dto;

import com.fiap.hortainteligente.Domain.TipoCultura;

public record CanteiroResponseDTO(
        Long id,
        String nome,
        double larguraMetros,
        double comprimentosMetros,
        double areaTotal,
        int umidadeAtualPercentual,
        TipoCultura tipoCultura,
        boolean precisaIrrigar

) {
}
