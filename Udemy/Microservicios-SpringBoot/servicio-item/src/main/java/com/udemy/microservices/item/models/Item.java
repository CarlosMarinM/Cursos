package com.udemy.microservices.item.models;

import com.udemy.microservices.commons.models.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Producto producto;
    private Integer cantidad;

}