package com.chalet.chaletapi.controller;

import com.chalet.chaletapi.dto.ProductoRequest;
import com.chalet.chaletapi.model.Producto;
import com.chalet.chaletapi.service.ProductoService;
import com.chalet.chaletapi.service.TipoProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return tipoProductoService.obtenerPorId(request.getTipoProductoId()).map(tipo -> {
            Producto producto = new Producto();
            producto.setNombre(request.getNombre());
            producto.setPrecio(request.getPrecio());
            producto.setStock(request.getStock());
            producto.setTipoProducto(tipo);
            return ResponseEntity.ok(productoService.guardar(producto));
        }).orElseGet(() -> ResponseEntity.badRequest().body(
                "TipoProducto no encontrado con ID: " + request.getTipoProductoId()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.obtenerPorId(id).map(productoExistente ->
                tipoProductoService.obtenerPorId(request.getTipoProductoId()).map(tipo -> {
                    productoExistente.setNombre(request.getNombre());
                    productoExistente.setPrecio(request.getPrecio());
                    productoExistente.setStock(request.getStock());
                    productoExistente.setTipoProducto(tipo);
                    return ResponseEntity.ok(productoService.guardar(productoExistente));
                }).orElse(ResponseEntity.badRequest().body("TipoProducto no encontrado con ID: " + request.getTipoProductoId()))
        ).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        return productoService.obtenerPorId(id).map(p -> {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
