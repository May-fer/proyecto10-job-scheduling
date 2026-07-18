
package main.java.com.modelo;

import java.util.List;

public class Resultado {

    private List<Machine> maquinas;
    private int valorOptimo;
    private long tiempoEjecucion;
    private long comparaciones;

    public Resultado() {
    }

    public Resultado(List<Machine> maquinas,
                     int valorOptimo,
                     long tiempoEjecucion,
                     long comparaciones) {

        this.maquinas = maquinas;
        this.valorOptimo = valorOptimo;
        this.tiempoEjecucion = tiempoEjecucion;
        this.comparaciones = comparaciones;
    }

    public List<Machine> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(List<Machine> maquinas) {
        this.maquinas = maquinas;
    }

    public int getValorOptimo() {
        return valorOptimo;
    }

    public void setValorOptimo(int valorOptimo) {
        this.valorOptimo = valorOptimo;
    }

    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(long tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public long getComparaciones() {
        return comparaciones;
    }

    public void setComparaciones(long comparaciones) {
        this.comparaciones = comparaciones;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "valorOptimo=" + valorOptimo +
                ", tiempoEjecucion=" + tiempoEjecucion +
                ", comparaciones=" + comparaciones +
                '}';
    }
}