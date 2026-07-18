package main.java.com.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ComparisonPanel extends GridPane {

    private final Label dpValor;
    private final Label greedyValor;
    private final Label bbValor;

    private final Label dpTiempo;
    private final Label greedyTiempo;
    private final Label bbTiempo;

    public ComparisonPanel() {

        setPadding(new Insets(15));
        setHgap(20);
        setVgap(10);

        add(new Label("Algoritmo"),0,0);
        add(new Label("Valor"),1,0);
        add(new Label("Tiempo (ms)"),2,0);

        add(new Label("Dynamic Programming"),0,1);
        add(new Label("Greedy"),0,2);
        add(new Label("Branch & Bound"),0,3);

        dpValor = new Label("-");
        greedyValor = new Label("-");
        bbValor = new Label("-");

        dpTiempo = new Label("-");
        greedyTiempo = new Label("-");
        bbTiempo = new Label("-");

        add(dpValor,1,1);
        add(greedyValor,1,2);
        add(bbValor,1,3);

        add(dpTiempo,2,1);
        add(greedyTiempo,2,2);
        add(bbTiempo,2,3);
    }

    public void actualizarDP(int valor,long tiempo){

        dpValor.setText(String.valueOf(valor));
        dpTiempo.setText(tiempo + " ms");

    }

    public void actualizarGreedy(int valor,long tiempo){

        greedyValor.setText(String.valueOf(valor));
        greedyTiempo.setText(tiempo + " ms");

    }

    public void actualizarBranchBound(int valor,long tiempo){

        bbValor.setText(String.valueOf(valor));
        bbTiempo.setText(tiempo + " ms");

    }
}