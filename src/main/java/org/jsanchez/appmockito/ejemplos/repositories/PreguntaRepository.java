package org.jsanchez.appmockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {
    void guardarVarias(List<String> preguntas);
    List<String> findPrguntasPorExamenId(Long id);
}
