package com.everton.dtos;

import com.everton.models.CategoriaModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoDto(@NotBlank String nome, @NotNull double preco, CategoriaModel categoria) {
}
