package com.example.grupo6.appgrup6;

public class Temperatura {
    private String fecha;
    private double temp;

    public Temperatura(String fecha, double temp){
        this.fecha = fecha;
        this.temp = temp;
    }

    public String getFecha() {
        return fecha;
    }

    public double getTemp() {
        return temp;
    }
}
