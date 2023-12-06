package com.example.juego;

/**
 * Excepción personalizada para manejar errores específicos del juego de conecta 4.
 * Esta excepción extiende la clase base {@link Exception} y proporciona constructores
 * para mensajes de error y causas subyacentes.
 *
 * Puedes utilizar esta excepción para capturar y gestionar situaciones excepcionales
 * que pueden ocurrir durante la ejecución del juego.
 */
public class GameExceptions extends Exception{
    /**
     * Constructor que crea una instancia de la excepción con un mensaje específico.
     * @param message Mensaje descriptivo de la excepción.
     */
    public GameExceptions(String message) {
        super(message);
    }
    /**
     * Constructor que crea una instancia de la excepción con un mensaje y una causa subyacente.
     * @param message Mensaje descriptivo de la excepción.
     * @param cause   Causa subyacente de la excepción.
     */
    public GameExceptions(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructor que crea una instancia de la excepción con una causa subyacente.
     * @param cause Causa subyacente de la excepción.
     */
    public GameExceptions(Throwable cause) {
        super(cause);
    }
}
