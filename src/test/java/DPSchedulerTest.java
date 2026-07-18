package test.java;

import main.java.com.modelo.Machine;
import main.java.com.modelo.Resultado;
import main.java.com.modelo.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas unitarias para DPScheduler.
class DPSchedulerTest {

    private DPScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new DPScheduler();
    }

    // Pruebas básicas
    @Test
    void testPlanificarConTareasVacias() {
        List<Task> tareas = new ArrayList<>();
        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertEquals(0, resultado.getValorOptimo());
        assertTrue(resultado.getMaquinas().isEmpty() || resultado.getMaquinas().get(0).getTareas().isEmpty());
    }

    @Test
    void testPlanificarUnaSolaTarea() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 10)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertEquals(10, resultado.getValorOptimo());
        assertEquals(1, resultado.getMaquinas().get(0).getTareas().size());
    }

    @Test
    void testPlanificarTareasCompatibles() {
        // Tareas que no se superponen: todas compatibles
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 5, 10, 4),
                new Task(3, 10, 15, 5)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertEquals(12, resultado.getValorOptimo()); // 3 + 4 + 5 = 12
        assertEquals(3, resultado.getMaquinas().get(0).getTareas().size());
    }

    @Test
    void testPlanificarTareasNoCompatibles() {
        // Tareas que se superponen: debe elegir la de mayor peso
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 10, 5),
                new Task(2, 2, 8, 10),
                new Task(3, 3, 7, 3)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        // Debe elegir la tarea 1 (peso 10) o una combinación compatible
        // Tarea 1 y 2 no son compatibles (se superponen)
        // Tarea 1 y 3 no son compatibles (se superponen)
        // La mejor opción es tarea 1 (peso 10) + tarea 3 (peso 3) = 13? No, se superponen
        // La mejor opción es tarea 1 (peso 10) o tarea 2 + tarea 3 (10 + 3 = 13) que no son compatibles
        // Tarea 2 y 3 no son compatibles (se superponen)
        // Debe elegir tarea 1 (peso 10) o tarea 2 (peso 10) o tarea 3 (peso 3)
        // La mejor opción es tarea 1 o tarea 2 = 10
        assertTrue(resultado.getValorOptimo() >= 10);
    }

    // Pruebas de casos específicos
    @Test
    void testPlanificarCasoEjemplo() {
        // Ejemplo típico de Weighted Interval Scheduling
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 3, 8, 4),
                new Task(3, 6, 10, 2),
                new Task(4, 2, 7, 5),
                new Task(5, 9, 12, 6)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        // La solución óptima debería ser: Tarea 1 (3) + Tarea 3 (2) + Tarea 5 (6) = 11
        // O Tarea 4 (5) + Tarea 5 (6) = 11
        // O Tarea 1 (3) + Tarea 5 (6) = 9
        // El óptimo es 11
        assertEquals(11, resultado.getValorOptimo());
    }

    @Test
    void testPlanificarConTareasDePesoCero() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 0),
                new Task(2, 3, 8, 0),
                new Task(3, 6, 10, 0)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertEquals(0, resultado.getValorOptimo());
        // Debe seleccionar algunas tareas (aunque peso 0)
        assertTrue(resultado.getMaquinas().get(0).getTareas().size() > 0);
    }

    // Pruebas de rendimiento y operaciones
    @Test
    void testComparacionesNoSonCero() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 3, 8, 4),
                new Task(3, 6, 10, 2)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        // Debe haber realizado comparaciones (BinarySearchUtil)
        assertTrue(resultado.getComparaciones() > 0);
    }

    @Test
    void testPlanificarConMuchasTareas() {
        // Probar con 100 tareas aleatorias
        List<Task> tareas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int inicio = i * 2;
            int fin = inicio + 3 + (i % 5);
            int peso = 1 + (i % 10);
            tareas.add(new Task(i, inicio, fin, peso));
        }

        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertTrue(resultado.getValorOptimo() > 0);
        assertTrue(resultado.getTiempoEjecucion() >= 0);
    }
}