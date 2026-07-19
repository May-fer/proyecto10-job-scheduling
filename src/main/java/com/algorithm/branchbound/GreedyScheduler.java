package com.epn.proyecto10.algorithm.greedy;

import com.epn.proyecto10.algorithm.Scheduler;
import com.epn.proyecto10.model.Machine;
import com.epn.proyecto10.model.Resultado;
import com.epn.proyecto10.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyScheduler implements Scheduler {

    private long comparaciones = 0;

    @Override
    public Resultado schedule(List<Task> tasks) {

        long inicioTiempo = System.nanoTime();

        comparaciones = 0;

        // Copiar la lista para no modificar la original
        List<Task> tareas = new ArrayList<>(tasks);

        /*
         * Heurística Greedy:
         * ordenar por tiempo de finalización.
         */
        tareas.sort(Comparator.comparingInt(Task::getFin));

        Machine maquina = new Machine(1);

        int ultimoFin = -1;
        int beneficio = 0;

        for (Task tarea : tareas) {

            comparaciones++;

            /*
             * Si la tarea no se cruza con la última
             * seleccionada, se acepta.
             */
            if (tarea.getInicio() >= ultimoFin) {

                maquina.agregarTarea(tarea);

                beneficio += tarea.getPeso();

                ultimoFin = tarea.getFin();
            }
        }

        long finTiempo = System.nanoTime();

        Resultado resultado = new Resultado();

        List<Machine> maquinas = new ArrayList<>();
        maquinas.add(maquina);

        resultado.setMaquinas(maquinas);
        resultado.setValorOptimo(beneficio);
        resultado.setComparaciones(comparaciones);
        resultado.setTiempoEjecucion(finTiempo - inicioTiempo);

        return resultado;
    }

    public long getComparaciones() {
        return comparaciones;
    }

    public String getNombre() {
        return "Greedy";
    }

}