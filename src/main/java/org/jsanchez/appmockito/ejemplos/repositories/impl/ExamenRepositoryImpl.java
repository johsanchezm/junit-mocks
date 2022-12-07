package org.jsanchez.appmockito.ejemplos.repositories.impl;

import org.jsanchez.appmockito.ejemplos.Datos;
import org.jsanchez.appmockito.ejemplos.models.Examen;
import org.jsanchez.appmockito.ejemplos.repositories.ExamenRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository  {
    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl.findAll");
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.EXAMENES;
    }
}
