package com.benchmark;

import com.algorithm.Scheduler;
import com.algorithm.dp.MultiMachineDP;
import com.modelo.Resultado;
import com.modelo.Task;

import java.util.List;

// Ejecuta benchmarks comparativos entre diferentes estrategias de scheduling
// Mide tiempo de ejecución, operaciones y valor óptimo.
public class BenchmarkRunner {

    // Ejecuta benchmarks comparativos entre diferentes schedulers
    public static TablaComparativa ejecutarBenchmark(List<List<Task>> casosPrueba,
                                                     int numMaquinas,
                                                     List<Scheduler> schedulers) {

        TablaComparativa tabla = new TablaComparativa();

        System.out.println("Iniciando benchmark con " + casosPrueba.size() + " casos...");

        for (int i = 0; i < casosPrueba.size(); i++) {
            List<Task> tareas = casosPrueba.get(i);
            System.out.println("Caso " + (i+1) + ": " + tareas.size() + " tareas");

            // Ejecutar MultiMachineDP (especial, no implementa Scheduler)
            MultiMachineDP multiDP = new MultiMachineDP();
            try {
                long startTime = System.nanoTime();
                Resultado resultado = multiDP.planificar(tareas, numMaquinas);
                long endTime = System.nanoTime();
                long tiempoMicros = (endTime - startTime) / 1000;

                tabla.agregarResultado(
                        "DP Multi-máquina",
                        tareas.size(),
                        resultado.getValorOptimo(),
                        resultado.getComparaciones(),
                        tiempoMicros,
                        calcularMakespan(resultado)
                );
            } catch (Exception e) {
                System.err.println("Error en DP Multi-máquina: " + e.getMessage());
                tabla.agregarError("DP Multi-máquina", tareas.size());
            }

            // Ejecutar cada scheduler
            for (Scheduler scheduler : schedulers) {
                try {
                    long startTime = System.nanoTime();
                    Resultado resultado = scheduler.planificar(tareas);
                    long endTime = System.nanoTime();
                    long tiempoMicros = (endTime - startTime) / 1000;

                    tabla.agregarResultado(
                            scheduler.getNombre(),
                            tareas.size(),
                            resultado.getValorOptimo(),
                            resultado.getComparaciones(),
                            tiempoMicros,
                            calcularMakespan(resultado)
                    );

                } catch (Exception e) {
                    System.err.println("Error en " + scheduler.getNombre() + ": " + e.getMessage());
                    tabla.agregarError(scheduler.getNombre(), tareas.size());
                }
            }
        }
        return tabla;
    }

    //Calcula el makespan (tiempo total) de un resultado
    private static long calcularMakespan(Resultado resultado) {
        if (resultado == null || resultado.getMaquinas() == null) {
            return 0;
        }

        long maxTiempo = 0;
        for (var maquina : resultado.getMaquinas()) {
            int tiempo = maquina.getTiempoTotal();
            if (tiempo > maxTiempo) {
                maxTiempo = tiempo;
            }
        }
        return maxTiempo;
    }
}
