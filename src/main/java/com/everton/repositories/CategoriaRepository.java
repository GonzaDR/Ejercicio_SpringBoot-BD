package com.everton.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.everton.models.CategoriaModel;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Integer> {

}
