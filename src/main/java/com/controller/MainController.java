package com.controller;

import com.algorithm.Scheduler;
import com.algorithm.branchbound.BranchAndBoundScheduler;
import com.algorithm.dp.DPScheduler;
import com.algorithm.dp.MultiMachineDP;
import com.algorithm.greedy.GreedyScheduler;
import com.modelo.Resultado;
import com.modelo.Task;
import com.view.ComparisonPanel;
import com.view.GanttChartView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Controlador principal que conecta la vista con los algoritmos.
public class MainController {

    // Elementos FXML - Deben coincidir con los IDs en main-view.fxml
    @FXML private TextArea txtAreaTareas;           // Entrada de tareas
    @FXML private TextField txtNumMaquinas;         // Número de máquinas
    @FXML private ComboBox<String> cmbEstrategia;   // Selector de algoritmo
    @FXML private Button btnEjecutar;               // Botón ejecutar
    @FXML private Button btnCargarCSV;              // Botón cargar CSV
    @FXML private Button btnLimpiar;                // Botón limpiar
    @FXML private Label lblEstado;                  // Label de estado

    // Contenedores donde se insertan los componentes de vista personalizados
    @FXML private VBox ganttContainer;
    @FXML private VBox comparisonContainer;

    // Componentes de la vista, creados en initialize() y agregados a los
    // contenedores de arriba (no son inyectados directamente por FXML
    // porque son componentes Java personalizados, no controles de FXML).
    private ComparisonPanel comparisonPanel;
    private GanttChartView ganttChartView;

    // Estado interno
    private List<Task> tareasActuales;
    private Resultado resultadoDP;
    private Resultado resultadoGreedy;
    private Resultado resultadoBranchBound;

    // Inicialización
    @FXML
    public void initialize() {
        //Configurar selector de estrategias
        cmbEstrategia.getItems().addAll(
                "DP (Una máquina)",
                "DP (Multi-máquina)",
                "Greedy (Voraz)",
                "Branch & Bound"
        );
        cmbEstrategia.setValue("DP (Una máquina)");

        //Crear e insertar los componentes de vista personalizados
        comparisonPanel = new ComparisonPanel();
        ganttChartView = new GanttChartView();
        comparisonContainer.getChildren().add(comparisonPanel);
        ganttContainer.getChildren().add(ganttChartView);

        //Configurar eventos
        btnEjecutar.setOnAction(e -> ejecutarAlgoritmo());
        btnCargarCSV.setOnAction(e -> cargarCSV());
        btnLimpiar.setOnAction(e -> limpiar());

        //Cargar ejemplo por defecto
        cargarEjemplo();

        //Estado inicial
        lblEstado.setText("Listo - Cargado ejemplo con 5 tareas");
    }

    // Cargar ejemplo por defecto
    private void cargarEjemplo() {
        txtAreaTareas.setText(
                "1, 0, 5, 3\n" +
                        "2, 3, 8, 4\n" +
                        "3, 6, 10, 2\n" +
                        "4, 2, 7, 5\n" +
                        "5, 9, 12, 6"
        );
        txtNumMaquinas.setText("2");
    }

    // Limpiar entrada y resultados
    private void limpiar() {
        txtAreaTareas.clear();
        txtNumMaquinas.setText("1");
        tareasActuales = null;
        resultadoDP = null;
        resultadoGreedy = null;
        resultadoBranchBound = null;
        lblEstado.setText("Listo");
    }

    // Parsear tareas desde texto
    private List<Task> parsearTareas(String texto) {
        List<Task> tareas = new ArrayList<>();
        String[] lineas = texto.trim().split("\n");

        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            String[] partes = linea.split(",\\s*");
            if (partes.length >= 4) {
                try {
                    int id = Integer.parseInt(partes[0]);
                    int inicio = Integer.parseInt(partes[1]);
                    int fin = Integer.parseInt(partes[2]);
                    int peso = Integer.parseInt(partes[3]);
                    tareas.add(new Task(id, inicio, fin, peso));
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando: " + linea);
                }
            }
        }
        return tareas;
    }

    // Ejecutar algoritmo seleccionado
    @FXML
    private void ejecutarAlgoritmo() {
        try {
            // 1. Validar entrada
            String texto = txtAreaTareas.getText();
            if (texto == null || texto.trim().isEmpty()) {
                lblEstado.setText("Error: No hay tareas");
                return;
            }

            // 2. Parsear tareas
            tareasActuales = parsearTareas(texto);
            if (tareasActuales.isEmpty()) {
                lblEstado.setText("Error: No se pudieron parsear las tareas");
                return;
            }

            // 3. Obtener número de máquinas
            int numMaquinas = 1;
            try {
                numMaquinas = Integer.parseInt(txtNumMaquinas.getText().trim());
                if (numMaquinas < 1) {
                    lblEstado.setText("Error: Máquinas debe ser >= 1");
                    return;
                }
            } catch (NumberFormatException e) {
                lblEstado.setText("Error: Número de máquinas inválido");
                return;
            }

            // 4. Obtener estrategia seleccionada
            String estrategia = cmbEstrategia.getValue();
            lblEstado.setText("Ejecutando " + estrategia + "...");

            // 5. Ejecutar según estrategia
            Resultado resultado = null;

            switch (estrategia) {
                case "DP (Una máquina)":
                    resultado = ejecutarDPUnaMaquina();
                    break;
                case "DP (Multi-máquina)":
                    resultado = ejecutarDPMultiMaquina(numMaquinas);
                    break;
                case "Greedy (Voraz)":
                    resultado = ejecutarGreedy();
                    break;
                case "Branch & Bound":
                    resultado = ejecutarBranchAndBound();
                    break;
                default:
                    lblEstado.setText("Estrategia no reconocida");
                    return;
            }

            // 6. Actualizar vista con resultados
            if (resultado != null) {
                actualizarVista(resultado, estrategia);
                lblEstado.setText("Estrategia " + estrategia + " completada");
            } else {
                lblEstado.setText("Error: Resultado nulo");
            }

        } catch (Exception e) {
            lblEstado.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Ejecutar DP (Una máquina)
    private Resultado ejecutarDPUnaMaquina() {
        Scheduler scheduler = new DPScheduler();
        Resultado resultado = scheduler.planificar(tareasActuales);
        resultadoDP = resultado;
        return resultado;
    }

    // Ejecutar DP (Multi-máquina)
    private Resultado ejecutarDPMultiMaquina(int numMaquinas) {
        MultiMachineDP multiDP = new MultiMachineDP();
        Resultado resultado = multiDP.planificar(tareasActuales, numMaquinas);
        resultadoDP = resultado;
        return resultado;
    }

    // Ejecutar Greedy
    private Resultado ejecutarGreedy() {
        Scheduler scheduler = new GreedyScheduler();
        Resultado resultado = scheduler.planificar(tareasActuales);
        resultadoGreedy = resultado;
        return resultado;
    }

    // Ejecutar Branch & Bound
    private Resultado ejecutarBranchAndBound() {
        Scheduler scheduler = new BranchAndBoundScheduler();
        Resultado resultado = scheduler.planificar(tareasActuales);
        resultadoBranchBound = resultado;
        return resultado;
    }

    // Actualizar la vista con resultados
    private void actualizarVista(Resultado resultado, String estrategia) {
        // 1. Actualizar ComparisonPanel
        if (comparisonPanel != null) {
            int valor = resultado.getValorOptimo();
            long tiempo = resultado.getTiempoEjecucion();

            switch (estrategia) {
                case "DP (Una máquina)":
                case "DP (Multi-máquina)":
                    comparisonPanel.actualizarDP(valor, tiempo);
                    break;
                case "Greedy (Voraz)":
                    comparisonPanel.actualizarGreedy(valor, tiempo);
                    break;
                case "Branch & Bound":
                    comparisonPanel.actualizarBranchBound(valor, tiempo);
                    break;
            }
        }

        // 2. Actualizar GanttChartView
        if (ganttChartView != null && resultado.getMaquinas() != null) {
            ganttChartView.mostrar(resultado.getMaquinas());
        }
    }

    // Cargar desde CSV
    @FXML
    private void cargarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar tareas desde CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );

        Stage stage = (Stage) btnCargarCSV.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String contenido = Files.readString(file.toPath());
                txtAreaTareas.setText(contenido);
                lblEstado.setText("CSV cargado: " + file.getName());
            } catch (Exception e) {
                lblEstado.setText("Error al cargar CSV: " + e.getMessage());
            }
        }
    }
}
