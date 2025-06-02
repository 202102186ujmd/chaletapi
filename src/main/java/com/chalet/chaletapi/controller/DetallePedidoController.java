package com.chalet.chaletapi.controller;

import com.chalet.chaletapi.dto.DetallePedidoRequest;
import com.chalet.chaletapi.model.DetallePedido;
import com.chalet.chaletapi.model.Pedido;
import com.chalet.chaletapi.model.Producto;
import com.chalet.chaletapi.service.DetallePedidoService;
import com.chalet.chaletapi.service.PedidoService;
import com.chalet.chaletapi.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<DetallePedido>> listarTodos() {
        return ResponseEntity.ok(detallePedidoService.listarTodos());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedido>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.listarPorPedido(pedidoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> obtenerPorId(@PathVariable Long id) {
        return detallePedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody DetallePedidoRequest request) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(request.getPedidoId());
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Pedido no encontrado con ID: " + request.getPedidoId());
        }

        Optional<Producto> productoOpt = productoService.obtenerPorId(request.getProductoId());
        if (productoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Producto no encontrado con ID: " + request.getProductoId());
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedidoOpt.get());
        detalle.setProducto(productoOpt.get());
        detalle.setCantidad(request.getCantidad());
        detalle.setSubtotal(request.getSubtotal());
        detalle.setPrecioUnitario(productoOpt.get().getPrecio());

        DetallePedido guardado = detallePedidoService.guardar(detalle);
        return ResponseEntity.ok(guardado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable Long id, @Valid @RequestBody DetallePedidoRequest request) {
        Optional<DetallePedido> detalleOpt = detallePedidoService.obtenerPorId(id);
        if (detalleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(request.getPedidoId());
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Pedido no encontrado con ID: " + request.getPedidoId());
        }

        Optional<Producto> productoOpt = productoService.obtenerPorId(request.getProductoId());
        if (productoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Producto no encontrado con ID: " + request.getProductoId());
        }

        DetallePedido detalleExistente = detalleOpt.get();
        detalleExistente.setPedido(pedidoOpt.get());
        detalleExistente.setProducto(productoOpt.get());
        detalleExistente.setCantidad(request.getCantidad());
        detalleExistente.setSubtotal(request.getSubtotal());
        detalleExistente.setPrecioUnitario(productoOpt.get().getPrecio());

        DetallePedido actualizado = detallePedidoService.guardar(detalleExistente);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        return detallePedidoService.obtenerPorId(id).map(dp -> {
            detallePedidoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
