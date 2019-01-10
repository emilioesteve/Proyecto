package com.example.grupo6.appgrup6;

public class Medicina {
    private String nombre;
    private double tomas;
    private String tiempo;

    public Medicina(String nombre, double tomas, String tiempo){
        this.nombre = nombre;
        this.tomas = tomas;
        this.tiempo = tiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTomas() {
        return tomas;
    }


    public String getTiempo() {
        return tiempo;
    }

}
