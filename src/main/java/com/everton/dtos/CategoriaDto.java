package com.everton.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDto(@NotBlank String descripcion) {

}
