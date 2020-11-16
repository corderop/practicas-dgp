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

public class Usuario {
    private String nombre;
    private String contrasena;
    private int cod_usuario;

    public Usuario (){
        nombre = null;
        contrasena = null;
        cod_usuario = 0;
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
