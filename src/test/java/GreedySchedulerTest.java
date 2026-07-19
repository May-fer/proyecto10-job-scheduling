

import com.algorithm.greedy.*;
import com.modelo.Resultado;
import com.modelo.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedySchedulerTest {

    private GreedyScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new GreedyScheduler();
    }

    @Test
    void testPlanificarConTareasVacias() {
        List<Task> tareas = new ArrayList<>();
        Resultado resultado = scheduler.planificar(tareas);

        assertNotNull(resultado);
        assertEquals(0, resultado.getValorOptimo());
    }

    @Test
    void testPlanificarUnaSolaTarea() {
        List<Task> tareas = Arrays.asList(new Task(1, 0, 5, 10));

        Resultado resultado = scheduler.planificar(tareas);

        assertEquals(10, resultado.getValorOptimo());
        assertEquals(1, resultado.getMaquinas().get(0).getTareas().size());
    }

    @Test
    void testPlanificarTareasCompatibles() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 5, 10, 4),
                new Task(3, 10, 15, 5)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertEquals(12, resultado.getValorOptimo());
        assertEquals(3, resultado.getMaquinas().get(0).getTareas().size());
    }

    @Test
    void testComparacionesNoSonCero() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 3, 8, 4)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertTrue(resultado.getComparaciones() > 0);
    }

    /**
     * Caso documentado: Greedy (orden por fin) NO encuentra el optimo.
     * Una tarea de peso muy alto se solapa con dos tareas de peso bajo
     * que "terminan antes", asi que Greedy las prefiere y descarta la
     * de mayor valor.
     */
    @Test
    void testGreedyNoEsOptimoFrenteADP() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 10, 100), // tarea valiosa, termina tarde
                new Task(2, 0, 2, 1),    // termina primero
                new Task(3, 2, 10, 1)    // termina "empatada" con la 1
        );

        Resultado resultadoGreedy = scheduler.planificar(tareas);

        // Greedy elige B + C = 1 + 1 = 2, en vez de A = 100
        assertEquals(2, resultadoGreedy.getValorOptimo());

        // DPScheduler (ver DPSchedulerTest) si encuentra 100 con este
        // mismo conjunto de tareas: esa es la evidencia de que Greedy
        // no garantiza optimalidad.
        assertTrue(resultadoGreedy.getValorOptimo() < 100);
    }
}
