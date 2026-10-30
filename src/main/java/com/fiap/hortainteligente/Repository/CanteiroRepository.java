package com.fiap.hortainteligente.Repository;

import com.fiap.hortainteligente.Domain.Canteiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//clase serve como uma ponte entre o sistema  e a base de dados sql

@Repository
public interface CanteiroRepository extends JpaRepository<Canteiro, Long> {
    //indica ao spring que esta interfac gera  a entidade Canteiro e o tipo de chave primaria é Long

    // Métodos como save(), findById(), findAll() e deleteById()
    // já estão prontos automaticamente através da herança do JpaRepository.
}
