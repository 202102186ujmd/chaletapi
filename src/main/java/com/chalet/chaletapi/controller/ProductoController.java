package com.chalet.chaletapi.controller;

import com.chalet.chaletapi.dto.ProductoRequest;
import com.chalet.chaletapi.model.Producto;
import com.chalet.chaletapi.model.TipoProducto;
import com.chalet.chaletapi.service.ProductoService;
import com.chalet.chaletapi.service.TipoProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final TipoProductoService tipoProductoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody ProductoRequest request) {
        Optional<TipoProducto> tipoOpt = tipoProductoService.obtenerPorId(request.getTipoProductoId());

        if (tipoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    "TipoProducto no encontrado con ID: " + request.getTipoProductoId()
            );
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setTipoProducto(tipoOpt.get());

        Producto productoGuardado = productoService.guardar(producto);
        return ResponseEntity.ok(productoGuardado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        Optional<Producto> productoOpt = productoService.obtenerPorId(id);
        if (productoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<TipoProducto> tipoOpt = tipoProductoService.obtenerPorId(request.getTipoProductoId());
        if (tipoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("TipoProducto no encontrado con ID: " + request.getTipoProductoId());
        }

        Producto productoExistente = productoOpt.get();
        productoExistente.setNombre(request.getNombre());
        productoExistente.setPrecio(request.getPrecio());
        productoExistente.setStock(request.getStock());
        productoExistente.setTipoProducto(tipoOpt.get());

        Producto actualizado = productoService.guardar(productoExistente);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        return productoService.obtenerPorId(id).map(p -> {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
