package com.fiap.hortainteligente.Service;


//regra de negocio
//todo tpo de calculo

import com.fiap.hortainteligente.Domain.Canteiro;
import com.fiap.hortainteligente.Mapper.CanteiroMapper;
import com.fiap.hortainteligente.Repository.CanteiroRepository;
import com.fiap.hortainteligente.dto.CanteiroRequestDTO;
import com.fiap.hortainteligente.dto.CanteiroResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanteiroService {

    private final CanteiroRepository repository;

    public CanteiroService(CanteiroRepository repository) {
        this.repository = repository;
    }

    public CanteiroResponseDTO cadastrar(CanteiroRequestDTO dto){
        //converte o dto em entidade
        Canteiro canteiro = CanteiroMapper.toEntity(dto);

        //grava a base de dados atreaves do repository
        Canteiro salvo = repository.save(canteiro);

        //alica as regaras de negocio
        double area = calcularArea(salvo.getComprimentoMetros(), salvo.getLarguraMetros());
        boolean precisaIrrigar = verificarNecessidadeDeIrrigacao(canteiro);

        return CanteiroMapper.toResposeDTO(salvo,area,precisaIrrigar);

    }

    private boolean verificarNecessidadeDeIrrigacao(Canteiro canteiro) {
        return  switch (canteiro.getTipoCultura()){
            case HORTALICA -> canteiro.getUmidadeAtualPercentual() < 60;
            case GRAO -> canteiro.getUmidadeAtualPercentual() < 50;
            case TUBERCULO -> canteiro.getUmidadeAtualPercentual() < 40;
        };

    }

    private double calcularArea(double comprimentoMetros, double larguraMetros) {
        return comprimentoMetros * larguraMetros;
    }

    public List<CanteiroResponseDTO> listartodos(){
        return repository.findAll().stream().map(canteiro -> {
            double area = calcularArea(canteiro.getLarguraMetros(), canteiro.getComprimentoMetros());
            boolean precisaIrrigar = verificarNecessidadeDeIrrigacao(canteiro);
            return CanteiroMapper.toResposeDTO(canteiro, area, precisaIrrigar);
        }).toList();
    }

    public CanteiroResponseDTO buscarPorId(Long id) {
        Canteiro canteiro = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canteiro com ID " + id + " não encontrado."));

        double area = calcularArea(canteiro.getLarguraMetros(), canteiro.getComprimentoMetros());
        boolean precisaIrrigar = verificarNecessidadeDeIrrigacao(canteiro);

        return CanteiroMapper.toResposeDTO(canteiro, area, precisaIrrigar);
    }
}
