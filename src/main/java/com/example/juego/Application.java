package com.example.juego;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que extiende {@link javafx.application.Application}.
 * Esta clase es responsable de iniciar la aplicación y cargar la interfaz
 * de usuario desde un archivo FXML.
 *
 * Puedes utilizar esta clase como punto de entrada para tu aplicación de Conecta 4 en JavaFX.
 */
public class Application extends javafx.application.Application {
    /**
     * Método principal que inicia la aplicación.
     * @param stage El objeto {@link Stage} principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("4 en raya");
        stage.setScene(scene);
        stage.show();

    }
    /**
     * Método principal que lanza la aplicación.
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        launch();
    }
}