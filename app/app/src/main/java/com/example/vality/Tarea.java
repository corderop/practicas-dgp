package com.example.vality;

// Librerías para las peticiones HTTP
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarea {

    private int cod_tarea;
    private int cod_facilitador;
    private String titulo;
    private String descripcion;
    private Date fecha_limite;
    private String objetivo;
    private String multimedia;
    private boolean realizada;
    private int calificacion;
/*
    // Constructor
    public Tarea(String url, RequestQueue queue, TextView tv){

        JSONObject respuesta;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cod_tarea = response.getInt("cod_tarea");
                            titulo = response.getString("titulo");
                            descripcion = response.getString("descripcion");
                            fecha_limite = new SimpleDateFormat("dd/MM/yyyy").parse(response.getString("fecha_limite"));
                            objetivo = response.getString("objetivo");
                            multimedia = response.getString("multimedia");
                            cod_facilitador = response.getInt("crea");
                            realizada = response.getBoolean("realizada");
                            calificacion = response.getInt("calificacion");

                            tv.setText(cod_tarea + "\n" +
                                    titulo + "\n" +
                                    descripcion + "\n" +
                                    fecha_limite.toString() + "\n" +
                                    objetivo + "\n" +
                                    multimedia + "\n" +
                                    cod_facilitador + "\n" +
                                    realizada + "\n" +
                                    calificacion + "\n"
                            );
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("ERROR");
                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
    }
*/
    public Tarea(){

    }

    public Tarea(int cod_tarea, int cod_facilitador, String titulo, String descripcion, Date fecha_limite, String objetivo, String multimedia, boolean realizada, int calificacion) {
        this.cod_tarea = cod_tarea;
        this.cod_facilitador = cod_facilitador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_limite = fecha_limite;
        this.objetivo = objetivo;
        this.multimedia = multimedia;
        this.realizada = realizada;
        this.calificacion = calificacion;
    }

    // ---------------------------------
    // GETTERS AND SETTERS
    // ---------------------------------

    public int getCod_tarea() {
        return cod_tarea;
    }

    public int getCod_facilitador() {
        return cod_facilitador;
    }

    public void setCod_facilitador(int code_facilitador) {
        this.cod_facilitador = code_facilitador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCod_Tarea(int code_tarea) {
        this.cod_tarea = code_tarea;
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
