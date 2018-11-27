package com.example.grupo6.appgrup6;

public class Peso {

    private String fecha;
    private double peso;

    public Peso(String fecha, double peso){
        this.fecha = fecha;
        this.peso = peso;
    }

    public Peso(double peso){
        this.peso = peso;
    }

    public Peso(String fecha){
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public double getPeso() {
        return peso;
    }

    public float getPesoFloat() {
        return (float) peso;
    }
}
