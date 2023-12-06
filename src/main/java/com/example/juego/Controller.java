package com.example.juego;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 /**
 * Controlador principal que gestiona la lógica del juego de conecta 4 en JavaFX.
 * Esta clase se encarga de manejar la interacción del usuario, la ejecución del
  * juego y la presentación visual.
 *
 * La lógica del juego incluye la gestión de turnos, la detección de victorias o
 * empates, y la actualización
 * de la interfaz gráfica para reflejar el estado actual del tablero.
 *
 * Además, el controlador permite personalizar la apariencia del juego, como el
 * color del tablero y de los jugadores.
 * También ofrece la funcionalidad de guardar el historial de partidas en un
 * archivo de texto.
 */
public class Controller {
    /**
     * ColorPicker que se encarga de cambiar el color del tablero.
     */
    @FXML
    public ColorPicker fondoColorPicker;
    /**
     * ColorPicker que se encarga de cambiar el color de las fichas
     * del jugador1.
     */
    @FXML
    public ColorPicker player1ColorPicker;
    /**
     * ColorPicker que se encarga de cambiar el color de las fichas
     * del jugador2.
     */
    @FXML
    public ColorPicker player2ColorPicker;
    /**
     * Tablero del juego.
     */
    @FXML
    GridPane tabla;
    /**
     * Opcion de IA VS IA.
     */
    @FXML
    private RadioButton maquina_maquina;
    /**
     * Opcion de Jugador VS IA.
     */
    @FXML
    private RadioButton jugador_maquina;
    /**
     * Opcion de Jugador VS Jugador.
     */
    @FXML
    private RadioButton jugador_jugador;
    /**
     * Texto que indica de quien es el turno.
     */
    @FXML
    Label turno;
    /**
     * Todas las fichas del juego.
     */
    @FXML
    private Circle c00,c01,c02,c03,c04,c05,c06,
            c10,c11,c12,c13,c14,c15,c16,
            c20,c21,c22,c23,c24,c25,c26,
            c30,c31,c32,c33,c34,c35,c36,
            c40,c41,c42,c43,c44,c45,c46,
            c50,c51,c52,c53,c54,c55,c56;

    /**
     * Array que continen todas las fichas del juego
     * ordenadas.
     */
    private Circle[][] piezas;
    /**
     * Jugador 1 es el que siempre que se inicia la aplicacion es
     * el primero en jugar y juega con fichas rojas de manera
     * predeterminada.
     */
    private Jugador jugador1 = new Jugador(Color.RED);
    /**
     * Jugador 2 es el que siempre que se inicia la aplicacion es
     * el segundo en jugar y juega con fichas amarillas de manera
     * predeterminada.
     */
    private Jugador jugador2 = new Jugador(Color.YELLOW);

    /**
     * Incializa la array que contine todas las fichas para poder
     * interactuar con todas ellas.
     */
    @FXML
    private void initialize(){
        piezas = new Circle[][]{
                {c00, c01, c02, c03, c04, c05, c06},
                {c10, c11, c12, c13, c14, c15, c16},
                {c20, c21, c22, c23, c24, c25, c26},
                {c30, c31, c32, c33, c34, c35, c36},
                {c40, c41, c42, c43, c44, c45, c46},
                {c50, c51, c52, c53, c54, c55, c56}
        };
    }

    /**
     * Alerta que aparece cuando se intenta hacer algo no
     * permitido como jugar en modo jugadorVSjugador sin
     * poner nombres a los jugadores o tambien cuando se
     * intenta poner una ficha en una columna que ya esta
     * llena.
     * @param mensaje Mensaje de error para cada situacion
     *                necesaria.
     */
    private void alert(String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ALERT");
        alert.setHeaderText("ERROR");
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    /**
     * Resetea el juego poniendo todas las fichas en
     * blanco y reiniciando las variables que controlan
     * las victorias y empates.
     */
    @FXML
    private void setReset(){
        if (win || empate){
            for (int x=0;x<6;x++){
                for(int y=0;y<7;y++){
                    piezas[x][y].setStyle("-fx-fill: #ffffff;");
                }
            }
            win=false;
            empate= false;
        }else {
            if (partida != null){
                partida.interrupt();
            }
            for (int x=0;x<6;x++){
                for(int y=0;y<7;y++){
                    piezas[x][y].setStyle("-fx-fill: #ffffff;");
                }
            }
            win=false;
            empate=false;
        }
    }

    /**
     * Permite cambiar el color del tablero en cualquier momento.
     */
    @FXML
    private void colorTablero(){
        String color = extraerParte(String.valueOf(fondoColorPicker.getValue()));
        tabla.setStyle("-fx-background-color: #"+color+";");
    }
    /**
     * Permite cambiar el color de las fichas del jugador 1
     * en cualquier momento.
     */
    @FXML
    private void colorPlaye1(){
        String color = extraerParte(String.valueOf(player1ColorPicker.getValue()));
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if (piezas[x][y].getFill().equals(jugador1.getColor())){
                    piezas[x][y].setStyle("-fx-fill: #"+color+";");
                }
            }
        }
        jugador1.setColor(Color.web(color));
    }
    /**
     * Permite cambiar el color de las fichas del jugador 2
     * en cualquier momento.
     */
    @FXML
    private void colorPlaye2(){
        String color = extraerParte(String.valueOf(player2ColorPicker.getValue()));
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if (piezas[x][y].getFill().equals(jugador2.getColor())){
                    System.out.println("a");
                    piezas[x][y].setStyle("-fx-fill: #"+color+";");
                }
            }
        }
        jugador2.setColor(Color.web(color));
    }

    /**
     * Usa regex en el color que devuelve el value de los elementos
     * color y los retorna como un codigo hexadecimal con un color
     * valido.
     * @param texto Valor que se obtiene al usar el getValue en un
     *              elemento Color.
     * @return Retorna un String con un hexadecimal que contiene
     * un codigo de color valido.
     */
    private static String extraerParte(String texto) {
        // Definir el patrón regex para encontrar la parte deseada
        Pattern patron = Pattern.compile("0x([a-fA-F0-9]+)ff");

        // Crear un objeto Matcher para buscar el patrón en el texto
        Matcher matcher = patron.matcher(texto);

        // Verificar si se encontró el patrón y obtener la parte deseada
        if (matcher.find()) {
            return matcher.group(1); // El grupo 1 contiene la parte deseada
        } else {
            return "No se encontró ninguna coincidencia";
        }
    }

    /**
     * Metodo que al seleccionar el RadioButton de jugador_mamquina
     * abre una ventana para poder ponerle un nombre al jugador1.
     */
    @FXML
    private void onJugadorMaquinaRadioButtonSelected() {
        if (jugador_maquina.isSelected()) {
            jugador1.setName("");
            nombreJugadorMaquina();
        }
    }

    /**
     * Popup que da la opcion de ponerle nombre al jugador1.
     */
    private void nombreJugadorMaquina() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre del Jugador");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingresa el nombre del jugador:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            jugador1.setName(nombre);
        });
    }
    /**
     * Metodo que al seleccionar el RadioButton de jugador_jugador
     * abre una ventana para poder ponerle un nombre al jugador1
     * y jugador2.
     */
    @FXML
    private void onJugadorJugadorRadioButtonSelected() {
        if (jugador_jugador.isSelected()) {
            jugador1.setName("");
            jugador2.setName("");
            nombreJugadorJugador();
        }
    }
    /**
     * Popup que da la opcion de ponerle nombre al jugador1
     * y al jugador2 poniendo una , entre los dos nombres.
     */
    private void nombreJugadorJugador() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Crear el diálogo de entrada
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombres de los Jugadores");
        dialog.setHeaderText(null);
        dialog.setGraphic(gridPane);
        dialog.setContentText("Ingresa el nombre del Jugador 1 y del Jugador 2 (separados por comas):");

        // Obtener los nombres de los jugadores
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombres -> {
            String[] nombresArray = nombres.split(",");
            String nombreJugador1 = nombresArray.length > 0 ? nombresArray[0].trim() : "";
            String nombreJugador2 = nombresArray.length > 1 ? nombresArray[1].trim() : "";

            jugador1.setName(nombreJugador1);
            jugador2.setName(nombreJugador2);
        });
    }

    /**
     * Variable que lleva el seguimiento de a que
     * jugador le toca jugar.
     */
    private boolean jugador1Turno = true;
    /**
     * Variable que lleva el seguimiento de la victoria
     * de cada partida.
     */
    private boolean win = false;
    /**
     * Varible que sirve para informar de si ha habido
     * un empate.
     */
    private boolean empate = false;
    /**
     * Permite que mientras el juego se ejecute puedas
     * realizar otras acciones y el juego no se congele.
     */
    private Thread partida;

    /**
     * Realiza la acción de jugar, gestionando los turnos y ejecutando la
     * lógica del juego del modo de juego que esteseleccionado. Tambien
     * gestiona los posibles errores que se pueden dar en el juego.
     * @throws GameExceptions Si ocurre un error relacionado con el juego.
     * @throws InterruptedException Si la ejecución del hilo se ve interrumpida.
     */
    @FXML
    private void onJugarClicked() throws GameExceptions, InterruptedException {
        turnos();
        if(maquina_maquina.isSelected()){
            jugador1.setName("maquina1");
            jugador2.setName("maquina2");
            partida = new Thread(() -> {
                    while (!win && !empate) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!win && !empate) partida();
                                    maquinaVSmaquina();

                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
            });
            partida.start();
        }else if(jugador_maquina.isSelected()) {
            if(jugador1.getName() != null && !jugador1.getName().isBlank()){
                jugador2.setName("maquina");
                partida = new Thread(() -> {
                    while (!win && !empate) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (!win && !empate) partida();
                                try {
                                    jugadorVSmaquina();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });
                partida.start();
            }else{
                alert(ExceptionMessage.JUGADOR1_NO_NAME);
                throw new GameExceptions(ExceptionMessage.JUGADOR1_NO_NAME);
            }
        } else if(jugador_jugador.isSelected()) {
            if((jugador1.getName() != null && jugador2.getName() != null) && !jugador1.getName().isBlank() && !jugador2.getName().isBlank()){
                partida = new Thread(() -> {
                    while (!win || !empate) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                turnos();
                                if (!win && !empate) partida();
                                jugadorVSjugador();
                            }
                        });
                    }
                });
                partida.start();
            }else if(jugador1.getName() == null && jugador2.getName() == null || jugador1.getName().isBlank() && jugador2.getName().isBlank()) {
                alert(ExceptionMessage.JUGADORES_NO_NAME);
                throw new GameExceptions(ExceptionMessage.JUGADORES_NO_NAME);
            }else if(jugador2.getName() == null || jugador2.getName().isBlank()){
                alert(ExceptionMessage.JUGADOR2_NO_NAME);
                throw new GameExceptions(ExceptionMessage.JUGADOR2_NO_NAME);
            }else if(jugador1.getName() == null || jugador1.getName().isBlank()){
                alert(ExceptionMessage.JUGADOR1_NO_NAME);
                throw new GameExceptions(ExceptionMessage.JUGADOR1_NO_NAME);
            }
        }
    }

    /**
     * Continene la logica de las maquinas y las hace jugar, comprobando en
     * cada momento quien gana.
     * @throws InterruptedException Si la ejecución del hilo se ve interrumpida.
     */
    private void maquinaVSmaquina() throws InterruptedException {
        int y=(int) (Math.random() * 7);
        if (jugador_maquina.isSelected()) turnos();
        if (!win && !empate){
            if(jugador1Turno){
                for(int x = 5; x >= 0; x--) {
                    if(extraerParte(piezas[x][y].getFill().toString()).equals("ffffff")){
                        String color = extraerParte(String.valueOf(jugador1.getColor()));
                        piezas[x][y].setStyle("-fx-fill: #"+color+";");
                        jugador1Turno=false;
                        break;
                    }
                }
            }else{
                for(int x = 5; x >= 0; x--) {
                    if(extraerParte(piezas[x][y].getFill().toString()).equals("ffffff")){
                        String color = extraerParte(String.valueOf(jugador2.getColor()));
                        piezas[x][y].setStyle("-fx-fill: #"+color+";");
                        jugador1Turno=true;
                        break;
                    }
                }
            }
        }
        if (maquina_maquina.isSelected()) turnos();
    }

    /**
     * Utiliza la logica de maquinaVSmaquina y jugadorVSjugador y va cambiando
     * de una a otra dependiendo de a que turno le toque, si le toca a
     * jugador1 usa jugadorVSjugador en cambio si le toca a jugador2 usa
     * maquinaVSmaquina.
     * @throws InterruptedException Si la ejecución del hilo se ve interrumpida.
     */
    private void jugadorVSmaquina() throws InterruptedException {
        if (!win && !empate){
            if(jugador1Turno){
                turnos();
                // Activa los circulos/piezas para que se puedan clickar.
                tabla.getChildren().forEach(node -> {
                    if (node instanceof Circle) {
                        ((Circle) node).setOnMouseClicked(event -> circleClickJvsJ());
                    }
                });
                circleClickJvsJ();
            }else {
                // Desactiva los circulos/piezas para que no se puedan clickar.
                tabla.getChildren().forEach(node -> {
                    if (node instanceof Circle) {
                        ((Circle) node).setOnMouseClicked(null);
                    }
                });
                maquinaVSmaquina();
            }
        }else{
            tabla.getChildren().forEach(node -> {
                if (node instanceof Circle) {
                    ((Circle) node).setOnMouseClicked(null);
                }
            });
        }
    }

    /**
     * Contiene la logica de jugador VS jugador y tambien realiza un
     * control en las piezas para permitir o no que se puedan clickar.
     */
    private void jugadorVSjugador(){
        if (!win && !empate){
            tabla.getChildren().forEach(node -> {
                if (node instanceof Circle) {
                    ((Circle) node).setOnMouseClicked(event -> circleClickJvsJ());
                }
            });
            circleClickJvsJ();
        }else{
            tabla.getChildren().forEach(node -> {
                if (node instanceof Circle) {
                    ((Circle) node).setOnMouseClicked(null);
                }
            });
        }
    }

    /**
     * Contiene el nombre del ganador.
     */
    String ganador;
    /**
     * Contiene el nombre del perdedor.
     */
    String perdedor;

    /**
     * Muestra una ventana con el ganador al finalizar la partida
     * si alguien ha ganado.
     * @param ganador Nombre del ganador
     */
    @FXML
    private void victoria(String ganador){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("VICTORIA");
        alert.setHeaderText("El ganador es: ");
        alert.setContentText(ganador);

        genererarTXT();
        alert.showAndWait();
    }

    /**
     * Muestra una ventana con el resultado de la partida
     * si ha habido un empate.
     */
    @FXML
    private void empate(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("FIN DE PARTIDA");
        alert.setHeaderText("EMPATE");
        alert.setContentText("Juega otra partida");

        genererarTXT();
        alert.showAndWait();
    }

    /**
     * Se encarga de mostrar el resultado de las partidas
     * y llamar al metodo que comprueba si alguien a
     * ganado.
     */
    private void partida(){
        win= comprobarVictoria();
        empate=comprobarEmpate();
        if (win) victoria(ganador);
        if (empate) empate();
    }

    /**
     * Llama a los metodos que comprueban la posible
     * vixctoria en todas las direcciones posibles.
     * @return Retorna un booleano con el resultado
     * de la partida.
     */
    private boolean comprobarVictoria() {
        return comprobarFilas() || comprobarColumnas() || comprobarDiagonales();
    }

    /**
     * Comprueba si el tablero esta lleno y no ha habido victoria.
     * @return Retorna un booleano que avisa si ha habido
     * o no empate.
     */
    private boolean comprobarEmpate() {
        boolean e=false;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if(piezas[x][y].getFill().equals(Color.WHITE)){
                    e=false;
                    break;
                }else e=true;
            }
            if (!e)break;
        }
        return e && !win;
    }

    /**
     * Comprueba si hay victoria en alguna fila.
     * @return Retorna un booleano que anuncia si hay o
     * no victoria.
     */
    private boolean comprobarFilas() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (piezas[i][j].getFill().equals(jugador1.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 3].getFill())) {
                    ganador = jugador1.getName();
                    perdedor = jugador2.getName();
                    return true;
                }else if (piezas[i][j].getFill().equals(jugador2.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i][j + 3].getFill())) {
                    ganador = jugador2.getName();
                    perdedor = jugador1.getName();
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Comprueba si hay victoria en alguna columna.
     * @return Retorna un booleano que anuncia si hay o
     * no victoria.
     */
    private boolean comprobarColumnas() {
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (piezas[i][j].getFill().equals(jugador1.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i + 1][j].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 2][j].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 3][j].getFill())) {
                    ganador = jugador1.getName();
                    perdedor = jugador2.getName();
                    return true;
                }else if (piezas[i][j].getFill().equals(jugador2.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i + 1][j].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 2][j].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 3][j].getFill())) {
                    ganador = jugador2.getName();
                    perdedor = jugador1.getName();
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Comprueba si hay victoria en alguna diagonal.
     * @return Retorna un booleano que anuncia si hay o
     * no victoria.
     */
    private boolean comprobarDiagonales() {
        // Comprobar diagonales descendentes
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (piezas[i][j].getFill().equals(jugador1.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i + 1][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 2][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 3][j + 3].getFill())) {
                    ganador = jugador1.getName();
                    perdedor = jugador2.getName();
                    return true;
                }else if (piezas[i][j].getFill().equals(jugador2.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i + 1][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 2][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i + 3][j + 3].getFill())) {
                    ganador = jugador2.getName();
                    perdedor = jugador1.getName();
                    return true;
                }
            }
        }

        // Comprobar diagonales ascendentes
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (piezas[i][j].getFill().equals(jugador1.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i - 1][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i - 2][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i - 3][j + 3].getFill())) {
                    ganador = jugador1.getName();
                    perdedor = jugador2.getName();
                    return true;
                }else if (piezas[i][j].getFill().equals(jugador2.getColor()) &&
                        piezas[i][j].getFill().equals(piezas[i - 1][j + 1].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i - 2][j + 2].getFill()) &&
                        piezas[i][j].getFill().equals(piezas[i - 3][j + 3].getFill())) {
                    ganador = jugador2.getName();
                    perdedor = jugador1.getName();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Comprueba si alguna pieza se ha clickado y si
     * es asi llama al metodo que pinta la primera
     * pieza blanca que haya en la columna comenzando desde
     * abajo.
     */
    @FXML
    private void circleClickJvsJ(){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int filaActual = i;
                int columnaActual = j;
                Circle pieza = piezas[i][j];

                pieza.setOnMouseClicked(e -> {
                    try {
                        comprobarColumna(filaActual, columnaActual);
                    } catch (GameExceptions ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }

    }

    /**
     * Comprueba todas las fichas de la columna donde se encuentra la
     * pieza clickada en busca de la primera pieza blanca empezando desde
     * abajo.
     * @param fila Posicion de la ficha en la fila.
     * @param columna Posicion de la ficha en la Columna.
     * @throws GameExceptions Si ocurre un error relacionado con el juego.
     */
    @FXML
    private void comprobarColumna(int fila, int columna) throws GameExceptions {
        boolean blancas = true;
        for (int i = 5; i >= 0; i--) {
            Circle pieza = piezas[i][columna];

            // Verificar si la ficha es blanca
            if (esBlanca(pieza)) {
                if (jugador1Turno){
                    pieza.setStyle("-fx-fill: #"+extraerParte(jugador1.getColor().toString())+";");
                    jugador1Turno=false;
                } else if(!jugador1Turno && jugador_jugador.isSelected()) {
                    pieza.setStyle("-fx-fill: #"+extraerParte(jugador2.getColor().toString())+";");
                    jugador1Turno=true;
                }
                blancas=true;
                break; // Terminar la búsqueda una vez que se ha encontrado una ficha blanca
            }else blancas=false;
        }
        if (!blancas) {
            alert(ExceptionMessage.COLUMNA_LLENA);
            throw new GameExceptions(ExceptionMessage.COLUMNA_LLENA);
        }
    }

    /**
     * Método para verificar si la ficha es blanca
     * @param pieza Pieza a comprobar.
     * @return Retorna un booleano que indica
     * si la ficha es blanca.
     */
    private boolean esBlanca(Circle pieza) {
        return pieza.getFill().equals(Color.WHITE);
    }

    /**
     * Mustra en pantalla de quien es el turno durante la
     * partida.
     */
    private void turnos(){
        if(jugador1Turno) turno.setText("JUGADOR 1");
        else turno.setText("JUGADOR 2");
    }

    /**
     * Cierra la aplicacion.
     */
    @FXML
    private void salir(){
        Platform.exit();
    }

    /**
     * Genera un fichero TXT con el historial de partidas.
     */
    public void genererarTXT() {
        String rutaArchivo = "src/main/resources/com/example/juego/informacion/partidas.txt";

        try (FileWriter fileWriter = new FileWriter(rutaArchivo, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Partida\n");
            if (empate) {
                bufferedWriter.write("Empate\n");
            }
            if (win) {
                bufferedWriter.write("Ganador: " + ganador + "\n");
                bufferedWriter.write("Perdedor: " + perdedor + "\n");
            }
            bufferedWriter.write("_".repeat(20) + "\n");

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Lee el fichero y muestra un popup con el historial
     * de partidas que se encuentra en el fichero.
     */
    @FXML
    private void mostrarPopupLeerArchivo() {
        String rutaArchivo = "src/main/resources/com/example/juego/informacion/partidas.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            mostrarPopup("HISTORIAL", contenido.toString());

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Popup con toda la informacion que se encuentra en el fichero.
     * @param titulo Titulo del popup.
     * @param contenido Toda la informacion del fichero.
     */
    private void mostrarPopup(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(null);

        VBox vBox = new VBox();
        Label label = new Label(contenido);

        // Agregar el label a un ScrollPane
        ScrollPane scrollPane = new ScrollPane(label);
        scrollPane.setPrefHeight(150);
        scrollPane.setPrefWidth(200);
        vBox.getChildren().add(scrollPane);

        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
    }

    /**
     * Popup que muestra la informacion del creador del programa.
     */
    @FXML
    private void mostrarAboutPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT");

        VBox vBox = new VBox();

        Label nombreLabel = new Label("Autor: Alejandro Parrilla Ruiz");
        vBox.getChildren().add(nombreLabel);

        Label github = new Label("Repositorio de GitHub:");
        vBox.getChildren().add(github);
        Hyperlink githubLink = new Hyperlink("https://github.com/AlejandroP02/UF5_objetos/tree/master/juego");
        vBox.getChildren().add(githubLink);

        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
    }
}