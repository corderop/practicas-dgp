package com.example.vality;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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

        pedirMensajes(this);
    }



    private void cargarVideo(String ruta) {
        VideoView videoView = this.findViewById(R.id.video_mensaje);
        MediaController mediaController = new MediaController(this);

        videoView.setVisibility(View.VISIBLE);

        Uri uri = Uri.parse("http://test.dgp.esy.es/" + ruta);
        System.out.println("Creamos mensaje para pedir video: " + uri);

        videoView.setVideoURI(uri);
        videoView.requestFocus();

        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
                mediaController.show();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                System.out.println("Ha ocurrido un problema con el vídeo");
                return false;
            }
        });
    }

    private void pedirMensajes(Chat context){
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
                            }
                            else {
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

        public void onEntrada(Mensaje entrada, View view) {
            System.out.println(entrada.getContenido());
            System.out.println(entrada.getMultimedia());

            if (entrada.getContenido() != "null") {
                //System.out.println("Vamos a cargar el texto:");
                TextView miTexto = view.findViewById(R.id.texto_mensaje);
                miTexto.setText(entrada.getContenido());
            }
            else if (entrada.getMultimedia() != "null") {
                String ruta = entrada.getMultimedia();
                if (entrada.getMultimedia().endsWith(".png") ||
                    entrada.getMultimedia().endsWith(".jpg") ||
                    entrada.getMultimedia().endsWith(".jpeg") ||
                    entrada.getMultimedia().endsWith(".webm")) {
                    String url = "http://test.dgp.esy.es/" + ruta;
                    ImageView imagen = Chat.this.findViewById(R.id.imagen_mensaje);
                    Picasso.get().load(url).into(imagen);
                    //cargarImagen(imagen, ruta);
                }
                else if (entrada.getMultimedia().endsWith(".mp4")) {
                    cargarVideo(ruta);
                }
            }

            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            TextView miFecha = view.findViewById(R.id.fecha_mensaje);
            miFecha.setText(formatoFecha.format(entrada.getFecha()));
        }
    }

    private void cargarImagen(ImageView imagen, String ruta){
        String url = "http://test.dgp.esy.es/" + ruta;
        imagen.setVisibility(View.VISIBLE);

        System.out.println("Creamos mensaje para pedir imagen: " + url);


        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imagen.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: "+ error);
            }
        }
        );
        queue.add(imageRequest);
    }
}
