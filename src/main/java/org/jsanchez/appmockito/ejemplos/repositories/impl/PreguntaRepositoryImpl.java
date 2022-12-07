package org.jsanchez.appmockito.ejemplos.repositories.impl;

import org.jsanchez.appmockito.ejemplos.Datos;
import org.jsanchez.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository {
    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVarias");

    }

    @Override
    public List<String> findPrguntasPorExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPrguntasPorExamenId");
        return Datos.PREGUNTAS;
    }
}
