package com.example.vality;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Usuario {
    private String nombre;
    private String contrasena;
    private int cod_usuario;
    private ArrayList<Tarea> tareas;
    private ListView lista;
    private MainActivity general;

    public Usuario (){
        nombre = null;
        contrasena = null;
        cod_usuario = 0;
        tareas = new ArrayList();
    }

    public void iniciar(String url, RequestQueue queue, TextView tv, String contrasenaIntroducida, MainActivity general) {
        try {
            this.general=general;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fotos", contrasenaIntroducida);

            System.out.println("Creando mensaje de logueo con contrasena: "+ contrasenaIntroducida);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nombre = response.getString("nombre");
                        cod_usuario = response.getInt("cod_usuario");
                        contrasena = contrasenaIntroducida;

                        if(nombre != null){
                            tv.setText("Logueo realizado con éxito");
                            tv.setTextColor(Color.BLACK);
                            Intent i = new Intent(general, tareasActivity.class);
                            i.putExtra("nombre", nombre);
                            i.putExtra("contrasena", contrasena);
                            i.putExtra("cod_usuario", cod_usuario+"");
                            general.startActivity(i);
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

    public void obtenerTareas(String url, RequestQueue queue, AppCompatActivity general, String formato){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("cod_usuario", cod_usuario);

            System.out.println("Creando mensaje para pedir tareas, cod_usuario: " + cod_usuario);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Ha llegado la lista con las tareas");
                        Tarea aux;
                        JSONObject tarea_Aux = null;
                        int contador =0;
                        boolean terminado = response.isNull(contador+"");
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        while(!terminado){
                            aux = new Tarea();
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
                            aux.setPictograma(tarea_Aux.getString("pictograma"));

                            if ( !aux.getRealizada()) {
                                tareas.add(aux);
                                System.out.println("Añadida tarea " + aux.toString());
                            }

                            contador++;
                            terminado = response.isNull(contador+"");
                        }
                        contador--;
                        System.out.println("La lista contenía " + contador + "tareas no realizadas, que son: ");
                        System.out.println(tareas.toString());

                        if(formato=="LISTA") {
                            System.out.println("pasamos a crear la interfaz de tareas");

                            lista = (ListView) general.findViewById(R.id.ListView_listado);
                            lista.setAdapter(new listaAdaptador(general, R.layout.tareasimple, tareas) {
                                @Override
                                public void onEntrada(Object entrada, View view) {
                                    if (entrada != null) {
                                        System.out.println(((Tarea) entrada).toString());
                                    /*System.out.print("Creamos una tarea en la interfaz de lista de tareas con valores titulo: " + ((Tarea) entrada).getTitulo() + " e imagen:");
                                    if (((Tarea) entrada).isRealizada()) {
                                        System.out.println(" bien");
                                    } else {
                                        System.out.println(" trabajar");
                                    }
                                    */

                                        TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                                        if (texto_superior_entrada != null) {
                                            texto_superior_entrada.setText(((Tarea) entrada).getTitulo());
                                        }

                                        TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                                        if (texto_inferior_entrada != null) {
                                            texto_inferior_entrada.setText("Pulse para acceder a la tarea");
                                        }

                                        ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                                        cargarImagen(imagen_entrada, ((Tarea) entrada).getPictograma(), queue);
                                    }
                                }
                            });

                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                                    Tarea elegida = (Tarea) pariente.getItemAtPosition(posicion);

                                    Intent i = new Intent(general, unaTareaActivity.class);

                                    i.putExtra("cod_tarea", elegida.getCod_tarea() + "");
                                    i.putExtra("cod_facilitador", elegida.getCod_facilitador() + "");
                                    i.putExtra("titulo", elegida.getTitulo() + "");
                                    i.putExtra("descripcion", elegida.getDescripcion() + "");
                                    i.putExtra("fecha_limite", elegida.getFecha_limite() + "");
                                    i.putExtra("objetivo", elegida.getObjetivo() + "");
                                    i.putExtra("multimedia", elegida.getMultimedia() + "");
                                    i.putExtra("realizada", elegida.getRealizada() + "");
                                    i.putExtra("calificacion", elegida.getCalificacion() + "");
                                    i.putExtra("cod_usuario", cod_usuario + "");
                                    i.putExtra("pictograma", elegida.getPictograma() + "");
                                    general.startActivity(i);
                                }
                            });
                        }else{                  //Estamos desde el calendario

                            Date diaActual = (Date) Calendar.getInstance().getTime();

                            Calendar calendar = Calendar.getInstance();
                            calendar.setFirstDayOfWeek( Calendar.MONDAY);
                            calendar.setMinimalDaysInFirstWeek( 4 );
                            calendar.setTime(diaActual);
                            int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                            int semanaTarea;

                            ((calendarioActivity)general).setTareasSemana(tareas, numberWeekOfYear);

                            System.out.println("Es la primera vez que creamos la lista de la semana "+ numberWeekOfYear);
                            ArrayList<Tarea> tareasLunes = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasMartes = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasMiercoles = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasJueves = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasViernes = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasSabado = new ArrayList<Tarea>();
                            ArrayList<Tarea> tareasDomingo = new ArrayList<Tarea>();
                            for(Tarea tarea : tareas){
                                calendar.setTime(tarea.getFecha_limite());
                                semanaTarea = calendar.get(Calendar.WEEK_OF_YEAR);
                                System.out.println("Tarea "+ tarea.getTitulo()+ " pertenece a la semana: "+ semanaTarea);
                                if(numberWeekOfYear == semanaTarea){
                                    System.out.print("Añadimos tarea a la semana");
                                    switch (calendar.get(Calendar.DAY_OF_WEEK)){
                                        case 1:
                                            System.out.println(" en domingo.");
                                            tareasDomingo.add(tarea);
                                            break;
                                        case 2:
                                            System.out.println(" en lunes.");
                                            tareasLunes.add(tarea);
                                            break;
                                        case 3:
                                            System.out.println(" en martes.");
                                            tareasMartes.add(tarea);
                                            break;
                                        case 4:
                                            System.out.println(" en miercoles.");
                                            tareasMiercoles.add(tarea);
                                            break;
                                        case 5:
                                            System.out.println(" en jueves.");
                                            tareasJueves.add(tarea);
                                            break;
                                        case 6:
                                            System.out.println(" en viernes.");
                                            tareasViernes.add(tarea);
                                            break;
                                        case 7:
                                            System.out.println(" en sabado.");
                                            tareasSabado.add(tarea);
                                            break;
                                    }
                                }
                            }
                            ((calendarioActivity)general).actualizarAdaptadorDia(1,tareasLunes);
                            ((calendarioActivity)general).actualizarAdaptadorDia(2,tareasMartes);
                            ((calendarioActivity)general).actualizarAdaptadorDia(3,tareasMiercoles);
                            ((calendarioActivity)general).actualizarAdaptadorDia(4,tareasJueves);
                            ((calendarioActivity)general).actualizarAdaptadorDia(5,tareasViernes);
                            ((calendarioActivity)general).actualizarAdaptadorDia(6,tareasSabado);
                            ((calendarioActivity)general).actualizarAdaptadorDia(7,tareasDomingo);
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
    private void cargarImagen(ImageView cuadroImagen, String multimedia, RequestQueue queue){
        String url="http://test.dgp.esy.es/"+ multimedia;
        System.out.println("Creamos mensaje para pedir imagen: "+ multimedia);

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        cuadroImagen.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: "+ error);
            }
        });
        queue.add(imageRequest);
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