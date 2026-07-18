
package main.java.com.modelo;

import java.util.ArrayList;
import java.util.List;

public class Machine {

    private int id;
    private List<Task> tareas;

    public Machine() {
        tareas = new ArrayList<>();
    }

    public Machine(int id) {
        this.id = id;
        tareas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Task> getTareas() {
        return tareas;
    }

    public void setTareas(List<Task> tareas) {
        this.tareas = tareas;
    }

    public void agregarTarea(Task tarea) {
        tareas.add(tarea);
    }

    public int getTiempoTotal() {
        int total = 0;

        for (Task tarea : tareas) {
            total += tarea.getDuracion();
        }

        return total;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", tareas=" + tareas.size() +
                '}';
    }
}
