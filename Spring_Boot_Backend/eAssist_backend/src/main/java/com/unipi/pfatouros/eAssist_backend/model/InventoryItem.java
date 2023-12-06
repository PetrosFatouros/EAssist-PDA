package com.unipi.pfatouros.eAssist_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryItem {

    @NotBlank
    private String name;

    @NotNull
    private Category category;

    @NotNull
    private Float price;

    @NotNull
    private Integer quantity;
}
