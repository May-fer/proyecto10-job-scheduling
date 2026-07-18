package main.java.com.algorithm.branchbound;

import main.java.com.algorithm.Scheduler;
import main.java.com.modelo.Task;
import main.java.com.modelo.Resultado;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyScheduler implements Scheduler {

    // Contador de operaciones para benchmarking
    private long operations = 0;

    @Override
    public Resultado schedule(List<Task> tasks) {

        // Reiniciar contador de operaciones
        operations = 0;

        // ==========================================
        // TODO 1:
        // Crear una copia de la lista para no modificar
        // la lista original recibida.
        // ==========================================
        List<Task> sortedTasks = new ArrayList<>(tasks);

        // ==========================================
        // TODO 2:
        // Elegir el criterio Greedy.
        //
        // Opción A:
        // Ordenar por tiempo de finalización.
        //
        // Opción B:
        // Ordenar por ratio peso/duración.
        //
        // Consultar con el grupo cuál utilizarán.
        // ==========================================
        sortedTasks.sort(Comparator.comparingInt(Task::getEnd));

        // Lista donde se guardarán las tareas elegidas
        List<Task> selectedTasks = new ArrayList<>();

        // Beneficio total
        int totalWeight = 0;

        // Fin de la última tarea seleccionada
        int lastFinish = -1;

        // ==========================================
        // TODO 3:
        // Recorrer todas las tareas.
        // ==========================================
        for (Task task : sortedTasks) {

            // Contar operaciones
            operations++;

            // ======================================
            // TODO 4:
            // Verificar que la tarea no se cruce
            // con la última seleccionada.
            //
            // Si no hay conflicto:
            //
            // - Agregar la tarea.
            // - Sumar el peso.
            // - Actualizar lastFinish.
            // ======================================
            if (task.getStart() >= lastFinish) {

                selectedTasks.add(task);

                totalWeight += task.getWeight();

                lastFinish = task.getEnd();
            }
        }

        // ==========================================
        // TODO 5:
        // Crear el objeto Resultado.
        // Dependerá de cómo Antony implemente esa clase.
        // ==========================================
        Resultado resultado = new Resultado();

        // ==========================================
        // TODO 6:
        // Guardar dentro del Resultado:
        //
        // - tareas seleccionadas
        // - beneficio total
        // - número de operaciones (opcional)
        //
        // Ejemplo:
        //
        // resultado.setTasks(selectedTasks);
        // resultado.setTotalWeight(totalWeight);
        //
        // Adaptar cuando exista Resultado.java
        // ==========================================

        return resultado;
    }

    @Override
    public long getOperations() {
        return operations;
    }

    @Override
    public String getName() {
        return "Greedy";
    }
}