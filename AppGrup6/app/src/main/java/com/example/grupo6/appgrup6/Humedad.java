package com.example.grupo6.appgrup6;

public class Humedad {
    private String fecha;
    private double humedad;

    public Humedad(String fecha, double humedad){
        this.fecha = fecha;
        this.humedad = humedad;
    }

    public String getFecha() {
        return fecha;
    }

    public double getHume() {
        return humedad;
    }
}
