package com.chalet.chaletapi.controller;

import com.chalet.chaletapi.dto.PedidoRequest;
import com.chalet.chaletapi.model.Pedido;
import com.chalet.chaletapi.model.Usuario;
import com.chalet.chaletapi.service.PedidoService;
import com.chalet.chaletapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody PedidoRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(request.getUsuarioId());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Usuario no encontrado con ID: " + request.getUsuarioId());
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(usuarioOpt.get());
        pedido.setFecha(request.getFecha());
        pedido.setEstado(request.getEstado());
        pedido.setTotal(request.getTotal());

        Pedido guardado = pedidoService.guardar(pedido);
        return ResponseEntity.ok(guardado);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(request.getUsuarioId());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Usuario no encontrado con ID: " + request.getUsuarioId());
        }

        Pedido pedidoExistente = pedidoOpt.get();
        pedidoExistente.setCliente(usuarioOpt.get());
        pedidoExistente.setFecha(request.getFecha());
        pedidoExistente.setEstado(request.getEstado());
        pedidoExistente.setTotal(request.getTotal());

        Pedido actualizado = pedidoService.guardar(pedidoExistente);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id).map(p -> {
            pedidoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
