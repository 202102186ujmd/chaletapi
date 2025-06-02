package com.chalet.chaletapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoProductoRequest {

    @NotBlank(message = "El nombre del tipo de producto es obligatorio")
    private String nombre;
}
