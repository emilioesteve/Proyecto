package com.example.grupo6.appgrup6;

public class Peso {

    private String fecha;
    private double peso;

    public Peso(String fecha, double peso){
        this.fecha = fecha;
        this.peso = peso;
    }

    public String getFecha() {
        return fecha;
    }

    public double getPeso() {
        return peso;
    }
}
