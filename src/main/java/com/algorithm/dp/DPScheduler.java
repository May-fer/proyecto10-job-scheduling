package main.java.com.algorithm.dp;

import main.java.com.algorithm.Scheduler;
import main.java.com.modelo.Machine;
import main.java.com.modelo.Resultado;
import main.java.com.modelo.Task;
import main.java.com.util.BinarySearchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DPScheduler implements Scheduler {

    @Override
    public String getNombre() {
        return "Programacion Dinamica (Weighted Interval Scheduling)";
    }

    @Override
    public Resultado planificar(List<Task> tareasOriginales) {
        long inicioTiempo = System.nanoTime();
        BinarySearchUtil.resetComparaciones();

        // 1. Ordenar las tareas por tiempo de fin ascendente (precondicion
        //    del algoritmo para poder calcular p(j) con busqueda binaria).
        List<Task> tareas = new ArrayList<>(tareasOriginales);
        tareas.sort(Comparator.comparingInt(Task::getFin));

        int n = tareas.size();
        Machine maquina = new Machine(0);

        if (n == 0) {
            long tiempoVacio = (System.nanoTime() - inicioTiempo) / 1_000_000;
            List<Machine> maquinasVacias = new ArrayList<>();
            maquinasVacias.add(maquina);
            return new Resultado(maquinasVacias, 0, tiempoVacio, 0L);
        }

        // 2. Calcular p(j) para cada tarea j (indice 0-based).
        int[] p = new int[n];
        for (int j = 0; j < n; j++) {
            p[j] = BinarySearchUtil.buscarPredecesorCompatible(tareas, j);
        }

        // 3. Construir la tabla DP de forma iterativa (bottom-up), evitando
        //    la recursion para no arrastrar overhead de pila en n grande.
        int[] opt = new int[n];
        opt[0] = tareas.get(0).getPeso();

        for (int j = 1; j < n; j++) {
            int incluirTarea = tareas.get(j).getPeso() + (p[j] == -1 ? 0 : opt[p[j]]);
            int excluirTarea = opt[j - 1];
            opt[j] = Math.max(incluirTarea, excluirTarea);
        }

        // 4. Reconstruir la solucion optima recorriendo la tabla hacia atras.
        List<Task> seleccionadas = reconstruirSolucion(tareas, p, opt, n - 1);

        // 5. Agrupar las tareas seleccionadas en la maquina (una sola, id = 0).
        for (Task tarea : seleccionadas) {
            tarea.setMaquina(maquina);
            maquina.agregarTarea(tarea);
        }

        long tiempoTotalMs = (System.nanoTime() - inicioTiempo) / 1_000_000;
        long comparaciones = BinarySearchUtil.getComparaciones();

        List<Machine> maquinas = new ArrayList<>();
        maquinas.add(maquina);

        return new Resultado(maquinas, opt[n - 1], tiempoTotalMs, comparaciones);
    }

    /**
     * Recorre la tabla DP desde la ultima tarea hasta la primera, decidiendo
     * en cada paso si la tarea j formo parte de la solucion optima o no,
     * segun cual de las dos ramas de la recurrencia produjo opt[j].
     */
    private List<Task> reconstruirSolucion(List<Task> tareas, int[] p, int[] opt, int j) {
        List<Task> seleccionadas = new ArrayList<>();

        while (j >= 0) {
            int valorSinTarea = (j == 0) ? 0 : opt[j - 1];
            int valorConTarea = tareas.get(j).getPeso() + (p[j] == -1 ? 0 : opt[p[j]]);

            if (valorConTarea >= valorSinTarea) {
                seleccionadas.add(0, tareas.get(j));
                j = p[j];
            } else {
                j = j - 1;
            }
        }

        return seleccionadas;
    }
}