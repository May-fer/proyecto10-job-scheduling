package com.view;

import com.modelo.Machine;
import com.modelo.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GanttChartView extends VBox {

    private final Pane panel;

    public GanttChartView() {

        setSpacing(10);
        setPadding(new Insets(10));

        Label titulo = new Label("Diagrama de Gantt");

        panel = new Pane();
        panel.setPrefSize(900, 400);

        getChildren().addAll(titulo, panel);
    }

    public void mostrar(List<Machine> maquinas) {

        panel.getChildren().clear();

        double altoFila = 60;
        double escala = 40;

        for (int i = 0; i < maquinas.size(); i++) {

            Machine maquina = maquinas.get(i);

            Label nombre = new Label("Máquina " + maquina.getId());
            nombre.setLayoutX(10);
            nombre.setLayoutY(i * altoFila + 20);

            panel.getChildren().add(nombre);

            for (Task tarea : maquina.getTareas()) {

                Rectangle rect = new Rectangle();

                rect.setX(120 + tarea.getInicio() * escala);
                rect.setY(i * altoFila + 10);
                rect.setWidth(tarea.getDuracion() * escala);
                rect.setHeight(35);

                rect.setFill(Color.LIGHTBLUE);
                rect.setStroke(Color.BLACK);

                Label id = new Label("T" + tarea.getId());
                id.setLayoutX(rect.getX() + 10);
                id.setLayoutY(rect.getY() + 8);

                panel.getChildren().addAll(rect, id);
            }
        }
    }

    public Pane getPanel() {
        return panel;
    }
}
