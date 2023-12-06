package com.example.juego;

import javafx.scene.paint.Color;

/**
 * Clase con la informacion de los jugadores.
 */
public class Jugador {
    /**
     * Nombre del jugador.
     */
    private String name;
    /**
     * Color de las fichas del jugador.
     */
    private Color color;

    /**
     * Constructor del jugador que solicita su color.
     * @param color Color de las fichas del jugador.
     */
    public Jugador(Color color) {
        this.color = color;
    }

    /**
     * Setter del nombre del jugador.
     * @param name Nuevo nombre para el jugador.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Setter del color del jugador.
     * @param color Nuevo color para las fichas del jugador.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter del nombre del jugador.
     * @return Retorna en String el nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter del color del jugador.
     * @return Retorna en Color el color del jugador.
     */
    public Color getColor() {
        return color;
    }
}
