package com.chalet.chaletapi.service;

import com.chalet.chaletapi.model.TipoProducto;
import com.chalet.chaletapi.repository.TipoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoProductoService {

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    public List<TipoProducto> listarTodos() {
        return tipoProductoRepository.findAll();
    }

    public Optional<TipoProducto> obtenerPorId(Long id) {
        return tipoProductoRepository.findById(id);
    }

    public TipoProducto guardar(TipoProducto tipoProducto) {
        return tipoProductoRepository.save(tipoProducto);
    }

    public void eliminar(Long id) {
        tipoProductoRepository.deleteById(id);
    }
}
