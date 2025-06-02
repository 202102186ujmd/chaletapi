package com.chalet.chaletapi.controller;

import com.chalet.chaletapi.dto.TipoProductoRequest;
import com.chalet.chaletapi.model.TipoProducto;
import com.chalet.chaletapi.service.TipoProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-productos")
@RequiredArgsConstructor
public class TipoProductoController {

    private final TipoProductoService tipoProductoService;

    @GetMapping
    public ResponseEntity<List<TipoProducto>> listarTodos() {
        return ResponseEntity.ok(tipoProductoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoProducto> obtenerPorId(@PathVariable Long id) {
        return tipoProductoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoProducto> crear(@Valid @RequestBody TipoProductoRequest request) {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setNombre(request.getNombre());
        return ResponseEntity.ok(tipoProductoService.guardar(tipoProducto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoProducto> actualizar(@PathVariable Long id, @Valid @RequestBody TipoProductoRequest request) {
        return tipoProductoService.obtenerPorId(id).map(tp -> {
            tp.setNombre(request.getNombre());
            return ResponseEntity.ok(tipoProductoService.guardar(tp));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        return tipoProductoService.obtenerPorId(id).map(tp -> {
            tipoProductoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
