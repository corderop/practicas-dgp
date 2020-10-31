package com.example.vality;

import java.util.Date;

public class Tarea {

    private int cod_tarea;
    private String titulo;
    private String descripcion;
    private Date fecha_limite;
    private String objetivo;
    private String multimedia;
    private int crea;
    private int realiza;
    private boolean realizada;
    private int calificacion;

    // Constructor
    public Tarea(int cod_tarea, String titulo, String descripcion, Date fecha_limite, String objetivo, String multimedia, int crea, int realiza, boolean realizada, int calificacion) {
        this.cod_tarea = cod_tarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_limite = fecha_limite;
        this.objetivo = objetivo;
        this.multimedia = multimedia;
        this.crea = crea;
        this.realiza = realiza;
        this.realizada = realizada;
        this.calificacion = calificacion;
    }

    // ---------------------------------
    // GETTERS AND SETTERS
    // ---------------------------------

    public int getCod_tarea() {
        return cod_tarea;
    }

    public int getCrea() {
        return crea;
    }

    public int getRealiza() {
        return realiza;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public boolean isRealizada() {
        return realizada;
    }

    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
