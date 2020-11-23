package com.example.vality;

// Librerías para las peticiones HTTP

import java.util.Date;

public class Mensaje {

    private int cod_mensaje;
    private Date fecha;
    private String contenido;
    private String multimedia;
    private boolean leido;
    private int cod_emisor;

    public Mensaje() {

    }

    public Mensaje(int cod_mensaje, Date fecha, String contenido, String multimedia, boolean leido, int cod_emisor) {
        this.cod_mensaje = cod_mensaje;
        this.fecha = fecha;
        this.contenido = contenido;
        this.multimedia = multimedia;
        this.leido = leido;
        this.cod_emisor = cod_emisor;
    }

    // ---------------------------------
    // GETTERS AND SETTERS
    // ---------------------------------

    public int getCod_mensaje() {
        return cod_mensaje;
    }

    public void setCod_mensaje(int cod_mensaje) {
        this.cod_mensaje = cod_mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public int getCod_emisor() {
        return cod_emisor;
    }

    public void setCod_emisor(int cod_emisor) {
        this.cod_emisor = cod_emisor;
    }
}