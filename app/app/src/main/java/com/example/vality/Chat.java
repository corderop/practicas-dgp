package com.example.vality;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {
    ListView listView;
    ArrayList<Mensaje> mensajes = new ArrayList<>();
    int cod_usuario;
    int cod_tarea;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cod_usuario = Integer.parseInt(getIntent().getStringExtra("cod_usuario"));
        cod_tarea = Integer.parseInt(getIntent().getStringExtra("cod_tarea"));

        setContentView(R.layout.chat);

        pedir_mensajes(this);

        /*
        Date fecha = new Date(2020, 12, 7);
        Mensaje msj1 = new Mensaje(1, fecha, "qoeupwhvqe9puhvpeqiuhvreipurhverpviuohqepiuvewrvergeqrgergqer", "", false, 1);
        Mensaje msj2 = new Mensaje(1, fecha, "Bien, ¿y tú?", "", false, 2);

        mensajes.add(msj1);
        mensajes.add(msj2);
        */
    }

    private void pedir_mensajes(Chat context){
        queue = Volley.newRequestQueue(context);
        String url = "http://test.dgp.esy.es/app/mensajes.php?cod_tarea="+this.cod_tarea;

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("cod_tarea", cod_tarea);

                System.out.println("Creando JSON para pedir mensaejes, cod_tarea: " + cod_tarea);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Ha llegado la lista con los mensajes");
                            Mensaje aux = new Mensaje();
                            JSONObject mensaje_Aux = null;
                            int contador =0;
                            boolean terminado = response.isNull(contador+"");
                            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                            while(!terminado){
                                mensaje_Aux = response.getJSONObject(contador+"");

                                if(mensaje_Aux != null){
                                    System.out.println("Leemos  JSON " + contador);
                                }else{
                                    System.out.println("Error el valor del JSON " + contador + " está vacío.");
                                }

                                aux.setCod_mensaje(mensaje_Aux.getInt("cod_mensaje"));
                                try {
                                    aux.setFecha(formatoFecha.parse(mensaje_Aux.getString("fecha")));
                                }catch (ParseException ex){
                                    System.out.println(ex);
                                }
                                aux.setContenido(mensaje_Aux.getString("contenido"));
                                aux.setMultimedia(mensaje_Aux.getString("multimedia"));
                                if(mensaje_Aux.getInt("leido") == 0){
                                    aux.setLeido(false);
                                }else{
                                    aux.setLeido(true);
                                }
                                aux.setCod_emisor(mensaje_Aux.getInt("envia"));

                                mensajes.add(aux);
                                System.out.println("Añadido mensaje " + aux.toString());
                                System.out.println("Añadido mensaje " + mensaje_Aux.toString());

                                aux=new Mensaje();


                                contador++;
                                terminado = response.isNull(contador+"");
                            }

                            System.out.println("La lista contenía " + contador + " mensajes");
                            System.out.println("Lista final de mensajes " + mensajes);

                            listView = (ListView) context.findViewById(R.id.list);
                            listView.setAdapter(new AdaptadorChat(context, R.layout.mensaje_enviado, mensajes, cod_usuario));

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

    class AdaptadorChat extends ArrayAdapter <Mensaje>{
        private Context contexto;
        private int R_layout_IdView;
        private ArrayList<Mensaje> mensajes;
        private int cod_usuario;

        public AdaptadorChat( Context context, int resource, ArrayList<Mensaje> msj, int cod_usuario) {
            super(context, resource);
            this.contexto = context;
            this.R_layout_IdView = resource;
            this.mensajes = new ArrayList<>(msj);
            this.cod_usuario=cod_usuario;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(mensajes.get(position).getCod_emisor() != this.cod_usuario){
                    System.out.println("Mostramos un mensaje recibido");
                    R_layout_IdView = R.layout.mensaje_recibido;
                }else{
                    System.out.println("Mostramos un mensaje enviado");
                    R_layout_IdView = R.layout.mensaje_enviado;
                }
                convertView = vi.inflate(R_layout_IdView, null);
            }
            onEntrada (mensajes.get(position), convertView);
            return convertView;
        }

        @Override
        public int getCount() {
            return mensajes.size();
        }

        @Override
        public Mensaje getItem(int posicion) {
            return mensajes.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

        public void onEntrada(Object entrada, View view) {
            TextView miTexto = view.findViewById(R.id.texto_mensaje);
            TextView miFecha = view.findViewById(R.id.fecha_mensaje);

            miTexto.setText(((Mensaje)entrada).getContenido());
            miFecha.setText(((Mensaje)entrada).getFecha().toString());
        }
    }
}
