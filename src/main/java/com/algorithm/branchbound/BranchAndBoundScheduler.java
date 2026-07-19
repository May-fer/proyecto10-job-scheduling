package com.algorithm.branchbound;

import com.algorithm.Scheduler;
import com.modelo.Machine;
import com.modelo.Resultado;
import com.modelo.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Branch & Bound para Weighted Interval Scheduling.
 *
 * Explora el arbol de decisiones "incluir tarea j / excluir tarea j"
 * en el orden de las tareas (ordenadas por fin), pero PODA una rama
 * cuando su cota superior optimista ya no puede superar la mejor
 * solucion encontrada hasta el momento. Esto evita explorar el arbol
 * completo (2^n) en la mayoria de los casos practicos, aunque en el
 * peor caso sigue siendo exponencial (por eso se compara su numero de
 * comparaciones contra DP, que es O(n log n)).
 */
public class BranchAndBoundScheduler implements Scheduler {

    // Contador de comparaciones: se incrementa en cada evaluacion de
    // cota, de compatibilidad, o al visitar un nodo del arbol.
    private long comparaciones = 0;

    private int mejorValorGlobal;
    private List<Task> mejorSeleccion;

    @Override
    public String getNombre() {
        return "Branch & Bound";
    }

    @Override
    public Resultado planificar(List<Task> tareasOriginales) {
        long inicioTiempo = System.nanoTime();
        comparaciones = 0;

        List<Task> tareas = new ArrayList<>(tareasOriginales);
        int n = tareas.size();
        Machine maquina = new Machine(0);

        if (n == 0) {
            long tiempoVacio = (System.nanoTime() - inicioTiempo) / 1_000_000;
            List<Machine> maquinasVacias = new ArrayList<>();
            maquinasVacias.add(maquina);
            return new Resultado(maquinasVacias, 0, tiempoVacio, 0L);
        }

        // Ordenar por fin ascendente, igual que DP y Greedy, para que
        // los resultados sean directamente comparables.
        tareas.sort(Comparator.comparingInt(Task::getFin));

        // Suma de pesos restantes desde cada indice (para la cota
        // superior): sufSuma[i] = suma de pesos de tareas[i..n-1]
        int[] sufSuma = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            sufSuma[i] = sufSuma[i + 1] + tareas.get(i).getPeso();
        }

        mejorValorGlobal = 0;
        mejorSeleccion = new ArrayList<>();

        explorar(tareas, 0, -1, 0, new ArrayList<>(), sufSuma);

        for (Task tarea : mejorSeleccion) {
            tarea.setMaquina(maquina);
            maquina.agregarTarea(tarea);
        }

        long tiempoTotalMs = (System.nanoTime() - inicioTiempo) / 1_000_000;

        List<Machine> maquinas = new ArrayList<>();
        maquinas.add(maquina);

        return new Resultado(maquinas, mejorValorGlobal, tiempoTotalMs, comparaciones);
    }

    /**
     * Recorre recursivamente el arbol de decisiones.
     *
     * @param tareas         lista ordenada por fin
     * @param indice         indice de la tarea que se esta decidiendo
     * @param ultimoFin      fin de la ultima tarea aceptada en esta rama
     * @param valorActual    suma de pesos aceptados hasta ahora en esta rama
     * @param seleccionActual tareas aceptadas hasta ahora en esta rama
     * @param sufSuma        suma de pesos restantes desde cada indice
     */
    private void explorar(List<Task> tareas, int indice, int ultimoFin,
                           int valorActual, List<Task> seleccionActual,
                           int[] sufSuma) {

        comparaciones++; // se cuenta la visita al nodo

        // Actualizar mejor solucion global si corresponde.
        if (valorActual > mejorValorGlobal) {
            mejorValorGlobal = valorActual;
            mejorSeleccion = new ArrayList<>(seleccionActual);
        }

        // Caso base: no quedan mas tareas por decidir.
        if (indice == tareas.size()) {
            return;
        }

        // Cota superior optimista: lo que ya tengo + el maximo posible
        // que podria sumar si aceptara TODAS las tareas restantes
        // (cota simple; ignora conflictos, por eso es "optimista").
        int cotaSuperior = valorActual + sufSuma[indice];
        comparaciones++;

        if (cotaSuperior <= mejorValorGlobal) {
            // Poda: ni en el mejor de los casos esta rama supera lo
            // que ya tenemos, asi que no vale la pena seguir.
            return;
        }

        Task actual = tareas.get(indice);
        comparaciones++;

        // Rama "incluir": solo si es compatible con la ultima aceptada.
        if (actual.getInicio() >= ultimoFin) {
            seleccionActual.add(actual);
            explorar(tareas, indice + 1, actual.getFin(),
                    valorActual + actual.getPeso(), seleccionActual, sufSuma);
            seleccionActual.remove(seleccionActual.size() - 1);
        }

        // Rama "excluir": se sigue sin tomar la tarea actual.
        explorar(tareas, indice + 1, ultimoFin, valorActual,
                seleccionActual, sufSuma);
    }

    public long getComparaciones() {
        return comparaciones;
    }
}
