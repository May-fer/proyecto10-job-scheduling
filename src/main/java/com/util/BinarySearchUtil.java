package com.util;

import com.modelo.Task;
import java.util.List;

//Utilidad para búsqueda binaria utilizada en algoritmos de programación
// de intervalos con peso (Weighted Interval Scheduling).
public class BinarySearchUtil {
    private static long comparaciones = 0;

    public static int buscarPredecesorCompatible(List<Task> tareas, int i) {
        if (i <= 0 || tareas == null || tareas.isEmpty()) {
            return -1;
        }

        Task tareaActual = tareas.get(i);
        int inicioActual = tareaActual.getInicio();

        int bajo = 0;
        int alto = i - 1;
        int resultado = -1;

        while(bajo <= alto) {
            comparaciones++;
            int medio = bajo + (alto - bajo) / 2;
            Task tareaMedia = tareas.get(medio);
            comparaciones++;

            if(tareaMedia.getFin() <= inicioActual) {
                resultado = medio;
                bajo = medio + 1;
            } else {
                alto = medio -1;
            }
        }
        return resultado;
    }

    public static int buscarPredecesorCompatible(long[] tiemposFin, int i) {
        if (i <= 0 || tiemposFin == null || tiemposFin.length == 0) {
            return -1;
        }

        long inicioActual = tiemposFin[i];
        int bajo = 0;
        int alto = i - 1;
        int resultado = -1;

        while(bajo <= alto) {
            comparaciones++;
            int medio = bajo + (alto - bajo) / 2;
            comparaciones++;
            if(tiemposFin[medio] <= inicioActual) {
                resultado = medio;
                bajo = medio + 1;
            } else {
                alto = medio -1;
            }
        }
        return resultado;
    }

    public static long getComparaciones() {
        return comparaciones;
    }

    public static void resetComparaciones() {
        comparaciones = 0;
    }

}
