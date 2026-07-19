package com.benchmark;

import com.modelo.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Generador de casos de prueba para benchmarks.
//Crea conjuntos de tareas con diferentes características.
public class TestCaseGenerator {

    private static final Random random = new Random(42); // Semilla fija para reproducibilidad

    //Genera múltiples casos de prueba con diferentes tamaños
    public static List<List<Task>> generarCasosPrueba(int numTareasBase, int numMaquinas, int numCasos) {
        List<List<Task>> casos = new ArrayList<>();

        for (int i = 0; i < numCasos; i++) {
            int tareas = numTareasBase + i * 10; // 50, 60, 70, 80, 90
            double densidad = 0.3 + i * 0.1; // 0.3, 0.4, 0.5, 0.6, 0.7
            casos.add(generarCasoPrueba(tareas, numMaquinas, densidad));
        }
        return casos;
    }

    // Genera un caso de prueba con parámetros específicos
    public static List<Task> generarCasoPrueba(int numTareas, int numMaquinas, double densidad) {
        List<Task> tareas = new ArrayList<>();
        long horizonte = 1000; // Horizonte temporal de 0 a 1000

        for (int i = 0; i < numTareas; i++) {
            long inicio = (long) (random.nextDouble() * horizonte * (1 - densidad * 0.5));
            long duracion = (long) (10 + random.nextDouble() * 50);
            long fin = inicio + duracion;
            int peso = 1 + random.nextInt(20);
            tareas.add(new Task(i, (int) inicio, (int) fin, peso));
        }
        return tareas;
    }

    //Genera un caso de prueba con tareas que tienen diferentes patrones (tareas cortas, largas, con pesos altos y bajos)
    public static List<Task> generarCasosDiversos(int numTareas) {
        List<Task> tareas = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numTareas; i++) {
            int inicio = rand.nextInt(100);
            int duracion;

            // Mezcla de tareas cortas, largas y medianas
            if (i % 3 == 0) {
                duracion = 5 + rand.nextInt(10); // Cortas
            } else if (i % 3 == 1) {
                duracion = 20 + rand.nextInt(30); // Largas
            } else {
                duracion = 10 + rand.nextInt(20); // Medianas
            }
            int fin = inicio + duracion;
            int peso = 1 + rand.nextInt(20);
            tareas.add(new Task(i, inicio, fin, peso));
        }
        return tareas;
    }
}
