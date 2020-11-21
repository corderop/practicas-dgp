package com.example.vality;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Usuario {
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

    public void iniciar(String url, RequestQueue queue, TextView tv, String usuarioIntroducido, String contrasenaIntroducida) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nombre", usuarioIntroducido);
            jsonBody.put("contrasena", contrasenaIntroducida);

            System.out.println("Creando mensaje nombre: "+usuarioIntroducido + " contrasena: "+ contrasenaIntroducida);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nombre = response.getString("nombre");
                        cod_usuario = response.getInt("cod_usuario");
                        contrasena = contrasenaIntroducida;

                        tv.setText("Logueo realizado con éxito");
                        obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue);
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
                        JSONObject tarea_Aux;
                        int contador =0;
                        boolean terminado = response.isNull(contador+"");
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        while(!terminado){
                            tarea_Aux = response.getJSONObject(contador+"");

                            aux.setCod_Tarea(response.getInt("cod_tarea"));
                            aux.setTitulo(response.getString("titulo"));
                            aux.setDescripcion(response.getString("descripcion"));
                            aux.setCod_facilitador(response.getInt("cod_facilitador"));
                            try {
                                aux.setFecha_limite(formatoFecha.parse(response.getString("fecha_limite")));
                            }catch (ParseException ex){
                                System.out.println(ex);
                            }
                            aux.setObjetivo(response.getString("objetivo"));
                            aux.setMultimedia(response.getString("multimedia"));
                            aux.setRealizada(response.getBoolean("realizada"));
                            aux.setCalificacion(response.getInt("calificacion"));

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
}
