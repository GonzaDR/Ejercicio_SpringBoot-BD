package com.everton.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.everton.models.ProductoModel;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Integer> {

}
