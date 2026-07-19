package com.algorithm.greedy;

import com.algorithm.Scheduler;
import com.modelo.Machine;
import com.modelo.Resultado;
import com.modelo.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Heuristica Greedy para Weighted Interval Scheduling.
 *
 * Criterio elegido: ordenar las tareas por tiempo de FIN ascendente y
 * aceptar cada tarea si es compatible (no se solapa) con la ultima
 * tarea aceptada. Este es el mismo criterio de ordenamiento que usa
 * DPScheduler, lo cual facilita comparar directamente ambas estrategias
 * en el mismo caso de prueba.
 *
 * Importante: este algoritmo es rapido (O(n log n)) pero NO garantiza
 * el valor optimo cuando los pesos de las tareas varian mucho (ver
 * metodo ordenarPorRatioPeso y la documentacion al final de la clase
 * para un caso concreto donde falla frente a DP).
 */
public class GreedyScheduler implements Scheduler {

    // Contador de comparaciones, mismo criterio que BinarySearchUtil:
    // se incrementa cada vez que se evalua una condicion de decision.
    private long comparaciones = 0;

    @Override
    public String getNombre() {
        return "Greedy (orden por fin)";
    }

    @Override
    public Resultado planificar(List<Task> tareasOriginales) {
        long inicioTiempo = System.nanoTime();
        comparaciones = 0;

        // Copia para no modificar la lista original recibida.
        List<Task> tareas = new ArrayList<>(tareasOriginales);

        int n = tareas.size();
        Machine maquina = new Machine(0);

        if (n == 0) {
            long tiempoVacio = (System.nanoTime() - inicioTiempo) / 1_000_000;
            List<Machine> maquinasVacias = new ArrayList<>();
            maquinasVacias.add(maquina);
            return new Resultado(maquinasVacias, 0, tiempoVacio, 0L);
        }

        ordenarPorFinTemprana(tareas);

        int ultimoFin = -1;
        int beneficioTotal = 0;

        for (Task tarea : tareas) {
            comparaciones++;

            // Compatible si empieza en o despues del fin de la ultima
            // tarea aceptada.
            if (tarea.getInicio() >= ultimoFin) {
                tarea.setMaquina(maquina);
                maquina.agregarTarea(tarea);

                beneficioTotal += tarea.getPeso();
                ultimoFin = tarea.getFin();
            }
        }

        long tiempoTotalMs = (System.nanoTime() - inicioTiempo) / 1_000_000;

        List<Machine> maquinas = new ArrayList<>();
        maquinas.add(maquina);

        return new Resultado(maquinas, beneficioTotal, tiempoTotalMs, comparaciones);
    }

    /**
     * Ordena las tareas por tiempo de fin ascendente. Es el criterio
     * que usa planificar() por defecto.
     */
    public void ordenarPorFinTemprana(List<Task> tareas) {
        tareas.sort(Comparator.comparingInt(Task::getFin));
    }

    /**
     * Criterio alternativo: ordenar por ratio peso/duracion descendente
     * (mayor "densidad de valor" primero). Se deja disponible por si el
     * equipo decide usar esta variante en vez de ordenar por fin;
     * pueden intercambiar la llamada dentro de planificar().
     *
     * OJO: con este criterio el chequeo de compatibilidad ya no puede
     * basarse solo en "ultimoFin" porque las tareas no quedan ordenadas
     * en el tiempo; habria que revisar contra TODAS las tareas ya
     * aceptadas. Se deja implementado el ordenamiento como referencia,
     * pero el bucle de seleccion de planificar() esta pensado para
     * ordenarPorFinTemprana().
     */
    public void ordenarPorRatioPeso(List<Task> tareas) {
        tareas.sort(Comparator.comparingDouble(
                (Task t) -> (double) t.getPeso() / Math.max(1, t.getDuracion())
        ).reversed());
    }

    public long getComparaciones() {
        return comparaciones;
    }

    
    
    /*
     * ============================================================
     * CASO DOCUMENTADO donde Greedy (orden por fin) NO es optimo:
     * ============================================================
     * Tarea A: inicio=0,  fin=10, peso=100
     * Tarea B: inicio=0,  fin=2,  peso=1
     * Tarea C: inicio=2,  fin=10, peso=1
     *
     * Greedy ordena por fin: B(fin=2) -> C(fin=10) -> A(fin=10)
     *   - Acepta B (peso 1), ultimoFin=2
     *   - Acepta C (peso 1) porque inicio(2) >= ultimoFin(2), ultimoFin=10
     *   - Rechaza A porque se solapa con C
     *   Resultado Greedy = 1 + 1 = 2
     *
     * DP (DPScheduler) evalua ambas ramas y encuentra que quedarse solo
     * con A da 100, que es mayor.
     *   Resultado DP = 100
     *
     * Conclusion: Greedy por fin ignora el peso de las tareas, por lo
     * que falla cuando hay una tarea de bajo "costo en tiempo" para
     * las demas pero de muchisimo mayor peso que ellas combinadas.
     * ============================================================
     */
}
