

import com.algorithm.greedy.GreedyScheduler;
import com.modelo.Resultado;
import com.modelo.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BranchAndBoundSchedulerTest {

    private BranchAndBoundScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new BranchAndBoundScheduler();
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
    }

    @Test
    void testPlanificarCasoEjemplo() {
        // Mismo caso usado en DPSchedulerTest.testPlanificarCasoEjemplo
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 3, 8, 4),
                new Task(3, 6, 10, 2),
                new Task(4, 2, 7, 5),
                new Task(5, 9, 12, 6)
        );

        Resultado resultado = scheduler.planificar(tareas);

        // El optimo (igual que en DP) es 11
        assertEquals(11, resultado.getValorOptimo());
    }

    @Test
    void testComparacionesNoSonCero() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 5, 3),
                new Task(2, 3, 8, 4),
                new Task(3, 6, 10, 2)
        );

        Resultado resultado = scheduler.planificar(tareas);

        assertTrue(resultado.getComparaciones() > 0);
    }

    /**
     * En este caso Greedy fallaba (ver GreedySchedulerTest); B&B debe
     * encontrar el mismo optimo que DP (100), a diferencia de Greedy.
     */
    @Test
    void testBranchAndBoundEncuentraOptimoQueGreedyPierde() {
        List<Task> tareas = Arrays.asList(
                new Task(1, 0, 10, 100),
                new Task(2, 0, 2, 1),
                new Task(3, 2, 10, 1)
        );

        Resultado resultadoBB = scheduler.planificar(tareas);
        Resultado resultadoGreedy = new GreedyScheduler().planificar(tareas);

        assertEquals(100, resultadoBB.getValorOptimo());
        assertTrue(resultadoBB.getValorOptimo() > resultadoGreedy.getValorOptimo());
    }

    @Test
    void testPlanificarConMuchasTareas() {
        List<Task> tareas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
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
