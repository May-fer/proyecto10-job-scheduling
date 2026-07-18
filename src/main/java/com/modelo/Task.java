package main.java.com.modelo;

public class Task {

    private int id;
    private int inicio;
    private int fin;
    private int peso;
    private Machine maquina;

    public Task() {
    }

    public Task(int id, int inicio, int fin, int peso) {
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
        this.peso = peso;
    }

    public Task(int id, int inicio, int fin, int peso, Machine maquina) {
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
        this.peso = peso;
        this.maquina = maquina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Machine getMaquina() {
        return maquina;
    }

    public void setMaquina(Machine maquina) {
        this.maquina = maquina;
    }

    public int getDuracion() {
        return fin - inicio;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", inicio=" + inicio +
                ", fin=" + fin +
                ", peso=" + peso +
                '}';
    }
}