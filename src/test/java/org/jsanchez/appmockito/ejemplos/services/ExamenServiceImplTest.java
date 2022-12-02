package org.jsanchez.appmockito.ejemplos.services;

import org.jsanchez.appmockito.ejemplos.models.Examen;
import org.jsanchez.appmockito.ejemplos.repositories.ExamenRepository;
import org.jsanchez.appmockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //con esto hace inidica el los mocks van por injección de dependencias en otra palabras no se tendría que usar el método setUp.
class ExamenServiceImplTest {
    @Mock
    ExamenRepository repository;

    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks //para usar esta anotación  se debe hacer sobre la calse implementada y no la intefaz
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this); //esta es una forma para los mock por injeccion de dependencia, las linas siguientes no aplican ya.
//        repository = mock(ExamenRepository.class);
//        preguntaRepository = mock(PreguntaRepository.class);
//        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPrguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Historia");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPrguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));

        verify(repository).findAll();
        verify(preguntaRepository).findPrguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPrguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertNull(examen);

        verify(repository).findAll();
        verify(preguntaRepository).findPrguntasPorExamenId(5L);
    }

    //desarrollo impulsado a comportamiento (given, when, then)
    @Test
    void testGuardarExamen()
    {
        //Given -> son las precondiciones de nuestro entorno de prueba
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
//        when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When, cuando se ejecuta un método del service que queremo probar sería el cuando.
        Examen examen = service.guardar(newExamen);

        //then, cuando validamos las respuestas de nuestro servicio probado.
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPrguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(preguntaRepository).findPrguntasPorExamenId(isNull());
    }
}