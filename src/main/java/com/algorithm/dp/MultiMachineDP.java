package com.algorithm.dp;

import com.modelo.Machine;
import com.modelo.Resultado;
import com.modelo.Task;

import java.util.ArrayList;
import java.util.List;

public class MultiMachineDP {

    private final DPScheduler dpUnaMaquina = new DPScheduler();

    /**
     * Planifica las tareas recibidas sobre 'numMaquinas' maquinas identicas.
     *
     * @param tareasOriginales conjunto de tareas a planificar
     * @param numMaquinas      numero de maquinas disponibles (k >= 1)
     * @return Resultado agregado: una Machine por cada maquina usada, con
     *         sus tareas ya asignadas (task.getMaquina() apunta a la
     *         maquina correcta), el valor optimo total, el tiempo total
     *         de ejecucion y el total de comparaciones realizadas.
     */
    public Resultado planificar(List<Task> tareasOriginales, int numMaquinas) {
        if (numMaquinas < 1) {
            throw new IllegalArgumentException("El numero de maquinas debe ser al menos 1");
        }

        long inicioTiempo = System.nanoTime();

        List<Task> disponibles = new ArrayList<>(tareasOriginales);
        List<Machine> maquinas = new ArrayList<>();
        int valorTotalAcumulado = 0;
        long comparacionesAcumuladas = 0;

        for (int idMaquina = 0; idMaquina < numMaquinas && !disponibles.isEmpty(); idMaquina++) {
            Resultado resultadoMaquina = dpUnaMaquina.planificar(disponibles);
            List<Task> seleccionadas = resultadoMaquina.getMaquinas().get(0).getTareas();

            // Se crea la maquina "real" con el id correcto y se reasignan
            // las tareas (DPScheduler internamente usa siempre id = 0).
            Machine maquinaFinal = new Machine(idMaquina);
            for (Task tarea : seleccionadas) {
                tarea.setMaquina(maquinaFinal);
                maquinaFinal.agregarTarea(tarea);
            }
            maquinas.add(maquinaFinal);

            valorTotalAcumulado += resultadoMaquina.getValorOptimo();
            comparacionesAcumuladas += resultadoMaquina.getComparaciones();

            // Se remueven las tareas ya asignadas antes de pasar a la
            // siguiente maquina, para que no se repitan entre maquinas.
            disponibles.removeAll(seleccionadas);
        }

        long tiempoTotalMs = (System.nanoTime() - inicioTiempo) / 1_000_000;

        return new Resultado(maquinas, valorTotalAcumulado, tiempoTotalMs, comparacionesAcumuladas);
    }
}
