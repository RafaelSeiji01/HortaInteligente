package com.fiap.hortainteligente.Controller;


import com.fiap.hortainteligente.Domain.Canteiro;
import com.fiap.hortainteligente.Service.CanteiroService;
import com.fiap.hortainteligente.dto.CanteiroRequestDTO;
import com.fiap.hortainteligente.dto.CanteiroResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canteiro")
@CrossOrigin(origins = "*") // Permite requisições do frontend local
public class CanteiroController {

    private final CanteiroService service;

    public CanteiroController(CanteiroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CanteiroResponseDTO> cadastrar(@Valid CanteiroRequestDTO dto){
        CanteiroResponseDTO response = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CanteiroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listartodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanteiroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
