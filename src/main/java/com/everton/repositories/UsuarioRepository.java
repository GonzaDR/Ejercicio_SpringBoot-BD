package com.everton.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.everton.models.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    List<UsuarioModel> findUsuarioByNomeLike(String nome);

    List<UsuarioModel> findUsuarioByTipo(String tipo);

}
