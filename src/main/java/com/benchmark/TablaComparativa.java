package com.benchmark;

import java.util.*;

// Tabla comparativa de resultados de benchmark.
// Almacena y formatea los resultados de diferentes estrategias.
public class TablaComparativa {

    private Map<String, List<FilaResultado>> resultados = new HashMap<>();

    public static class FilaResultado {
        public int numTareas;
        public long valorOptimo;
        public long operaciones;
        public long tiempoMicros;
        public long makespan;
        public boolean error = false;

        public FilaResultado(int numTareas, long valorOptimo, long operaciones,
                             long tiempoMicros, long makespan) {
            this.numTareas = numTareas;
            this.valorOptimo = valorOptimo;
            this.operaciones = operaciones;
            this.tiempoMicros = tiempoMicros;
            this.makespan = makespan;
        }

        public FilaResultado(int numTareas, boolean error) {
            this.numTareas = numTareas;
            this.error = error;
        }
    }

    public void agregarResultado(String estrategia, int numTareas, long valorOptimo,
                                 long operaciones, long tiempoMicros, long makespan) {
        resultados.computeIfAbsent(estrategia, k -> new ArrayList<>())
                .add(new FilaResultado(numTareas, valorOptimo, operaciones, tiempoMicros, makespan));
    }

    public void agregarError(String estrategia, int numTareas) {
        resultados.computeIfAbsent(estrategia, k -> new ArrayList<>())
                .add(new FilaResultado(numTareas, true));
    }

    public Map<String, List<FilaResultado>> getResultados() {
        return resultados;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(100)).append("\n");
        sb.append("BENCHMARK COMPARATIVO\n");
        sb.append("=".repeat(100)).append("\n\n");

        if (resultados.isEmpty()) {
            sb.append("No hay resultados para mostrar.\n");
            return sb.toString();
        }

        // Ordenar estrategias alfabéticamente
        List<String> estrategias = new ArrayList<>(resultados.keySet());
        Collections.sort(estrategias);

        // Cabecera de la tabla
        sb.append(String.format("%-20s | %8s | %10s | %12s | %10s | %10s | %10s\n",
                "Estrategia", "Tareas", "Valor Opt.", "Operaciones", "Tiempo μs", "Makespan", "Estado"));
        sb.append("-".repeat(100)).append("\n");

        // Datos por estrategia
        for (String estrategia : estrategias) {
            List<FilaResultado> filas = resultados.get(estrategia);

            // Mostrar filas individuales
            for (FilaResultado f : filas) {
                if (f.error) {
                    sb.append(String.format("%-20s | %8d | %10s | %12s | %10s | %10s | %10s\n",
                            estrategia, f.numTareas, "ERROR", "ERROR", "ERROR", "ERROR"));
                } else {
                    sb.append(String.format("%-20s | %8d | %10d | %12d | %10d | %10d | %10s\n",
                            estrategia,
                            f.numTareas,
                            f.valorOptimo,
                            f.operaciones,
                            f.tiempoMicros,
                            f.makespan));
                }
            }

            // Calcular y mostrar promedio para esta estrategia
            long sumValor = 0, sumOps = 0, sumTime = 0, sumMakespan = 0;
            int count = 0;
            for (FilaResultado f : filas) {
                if (!f.error) {
                    sumValor += f.valorOptimo;
                    sumOps += f.operaciones;
                    sumTime += f.tiempoMicros;
                    sumMakespan += f.makespan;
                    count++;
                }
            }

            if (count > 0) {
                sb.append(String.format("%-20s | %8s | %10d | %12d | %10d | %10d | %10s\n",
                        "└─ PROMEDIO",
                        "",
                        sumValor / count,
                        sumOps / count,
                        sumTime / count,
                        sumMakespan / count));
            }
            sb.append("-".repeat(100)).append("\n");
        }
        return sb.toString();
    }

    // Exporta los resultados a formato CSV
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estrategia,Tareas,ValorOptimo,Operaciones,TiempoMicros,Makespan\n");

        for (var entry : resultados.entrySet()) {
            String estrategia = entry.getKey();
            for (FilaResultado f : entry.getValue()) {
                if (!f.error) {
                    sb.append(String.format("%s,%d,%d,%d,%d,%d\n",
                            estrategia, f.numTareas, f.valorOptimo, f.operaciones, f.tiempoMicros, f.makespan));
                }
            }
        }
        return sb.toString();
    }
}
