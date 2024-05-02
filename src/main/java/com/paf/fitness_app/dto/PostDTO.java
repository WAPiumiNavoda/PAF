package com.paf.fitness_app.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record PostDTO(
                     Long id,
                     String image,

                     @NotEmpty(message = "Car should have a brand") String brand,
                     @NotEmpty(message = "Car should have a model") String model,
                     @NotNull(message = "Car should have a year") Integer year) {
}
