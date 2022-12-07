package org.jsanchez.appmockito.ejemplos.services;

import org.jsanchez.appmockito.ejemplos.Datos;
import org.jsanchez.appmockito.ejemplos.models.Examen;
import org.jsanchez.appmockito.ejemplos.repositories.ExamenRepository;
import org.jsanchez.appmockito.ejemplos.repositories.PreguntaRepository;
import org.jsanchez.appmockito.ejemplos.repositories.impl.ExamenRepositoryImpl;
import org.jsanchez.appmockito.ejemplos.repositories.impl.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //con esto hace inidica el los mocks van por injección de dependencias en otra palabras no se tendría que usar el método setUp.
class ExamenServiceImplSpyTest {
    @Spy
    ExamenRepositoryImpl repository;

    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks //para usar esta anotación  se debe hacer sobre la calse implementada y no la intefaz
    ExamenServiceImpl service;

    @Test
    void testSpy() {
      service = new ExamenServiceImpl(repository, preguntaRepository);

        List<String> preguntas = Arrays.asList("arimetica");

//        when(preguntaRepository1.findPrguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPrguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("arimetica"));

        verify(repository).findAll();
        verify(preguntaRepository).findPrguntasPorExamenId(anyLong());
    }
}