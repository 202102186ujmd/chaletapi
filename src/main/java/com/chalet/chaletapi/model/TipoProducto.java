package com.chalet.chaletapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipos_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "tipoProducto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Producto> productos;
}
