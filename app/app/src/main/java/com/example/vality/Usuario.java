package com.example.vality;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Usuario{
    private String nombre;
    private String contrasena;
    private int cod_usuario;
    ArrayList<Tarea> tareas;

    public Usuario (){
        nombre = null;
        contrasena = null;
        cod_usuario = 0;
        tareas = new ArrayList();
    }

    public void iniciar(String url, RequestQueue queue, TextView tv, String contrasenaIntroducida, MainActivity general) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("contrasena", contrasenaIntroducida);

            System.out.println("Creando mensaje de logueo con contrasena: "+ contrasenaIntroducida);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nombre = response.getString("nombre");
                        cod_usuario = response.getInt("cod_usuario");
                        contrasena = contrasenaIntroducida;

                        tv.setText("Logueo realizado con éxito");
                        tv.setTextColor(Color.BLACK);
                        obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue);

                        if(nombre != null){
                            tv.setText("Credenciales correctos");
                        }else {
                            general.borrarContrasena();
                            tv.setText("Credenciales inválidos");
                            tv.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                    System.out.println("ERROR: "+ error);
                }
            });

            // Access the RequestQueue through your singleton class.
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void obtenerTareas(String url, RequestQueue queue){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("cod_usuario", cod_usuario);

            System.out.println("Creando mensaje para pedir tareas, cod_usuario: " + cod_usuario);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Ha llegado la lista con las tareas");
                        Tarea aux = new Tarea();
                        JSONObject tarea_Aux = null;
                        int contador =0;
                        boolean terminado = response.isNull(contador+"");
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        while(!terminado){


                            tarea_Aux = response.getJSONObject(contador+"");

                            if(tarea_Aux != null){
                                System.out.println("Leemos  tarea " + contador);
                            }else{
                                System.out.println("Error el valor de la tarea " + contador + " está vacío.");
                            }

                            aux.setCod_Tarea(tarea_Aux.getInt("cod_tarea"));
                            aux.setTitulo(tarea_Aux.getString("titulo"));
                            aux.setDescripcion(tarea_Aux.getString("descripcion"));
                            aux.setCod_facilitador(tarea_Aux.getInt("cod_facilitador"));
                            try {
                                aux.setFecha_limite(formatoFecha.parse(tarea_Aux.getString("fecha_limite")));
                            }catch (ParseException ex){
                                System.out.println(ex);
                            }
                            aux.setObjetivo(tarea_Aux.getString("objetivo"));
                            aux.setMultimedia(tarea_Aux.getString("multimedia"));
                            if(tarea_Aux.getInt("realizada") == 0){
                                aux.setRealizada(false);
                            }else{
                                aux.setRealizada(true);
                            }
                            if(tarea_Aux.isNull("calificacion")){
                                aux.setCalificacion(-1);
                            }else{
                                aux.setCalificacion(tarea_Aux.getInt("calificacion"));
                            }

                            tareas.add(aux);
                            System.out.println("Añadida tarea " + tarea_Aux.toString());

                            contador++;
                            terminado = response.isNull(contador+"");
                        }
                        System.out.println("La lista contenía " + contador + "tareas");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                    System.out.println("ERROR: "+ error);
                }
            });



            // Access the RequestQueue through your singleton class.
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(int cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }
}
