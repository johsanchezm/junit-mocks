package org.jsanchez.appmockito.ejemplos.repositories;

import org.jsanchez.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}
