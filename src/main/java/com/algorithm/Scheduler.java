package main.java.com.algorithm;

import main.java.com.modelo.Resultado;
import main.java.com.modelo.Task;

import java.util.List;


public interface Scheduler {

    /**
     * Ejecuta la estrategia de planificacion sobre el conjunto de tareas
     * recibido y devuelve el resultado (tareas seleccionadas, valor total,
     * tiempo de ejecucion y numero de comparaciones).
     */
    Resultado planificar(List<Task> tareas);

    /**
     * Nombre legible de la estrategia, usado para mostrar resultados en
     * la interfaz y en las tablas comparativas de benchmarking.
     */
    String getNombre();
}