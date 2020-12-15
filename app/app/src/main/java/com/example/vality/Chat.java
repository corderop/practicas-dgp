package com.example.vality;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    ListView listView;
    ArrayList<Mensaje> mensajes = new ArrayList<>();
    int cod_usuario;
    int cod_tarea;
    String titulo_tarea;
    RequestQueue queue;

    MediaPlayer mp = new MediaPlayer();
    TextView tiempoAudio, duracionAudio;
    SeekBar barraAudio;
    Button botonAudio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cod_usuario = Integer.parseInt(getIntent().getStringExtra("cod_usuario"));
        cod_tarea = Integer.parseInt(getIntent().getStringExtra("cod_tarea"));
        titulo_tarea = getIntent().getStringExtra("titulo_tarea");

        setContentView(R.layout.chat);

        TextView tituloView = this.findViewById(R.id.titulo_chat);
        tituloView.setText(titulo_tarea.toUpperCase());

        pedirMensajes(this);
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
                        listView.setAdapter(new AdaptadorChat(context, R.layout.texto_enviado, mensajes, cod_usuario));

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

    public void enviarMensaje (View v) {
        /*
        String rutaArchivo = null;
        String nombreArchivo = rutaArchivo;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        int respuestaServidor = 0;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File archivo = new File(rutaArchivo);

        if (!archivo.isFile()) {

            Log.e("uploadFile", "Source File not exist : " + rutaArchivo);

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :" + uploadFilePath + "" + uploadFileName);
                }
            });

            //return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(archivo);
                URL url = new URL("test.dgp.esy.es/app/mensaje.php");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", nombreArchivo);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="+ nombreArchivo + "" + lineEnd);

                   dos.writeBytes(lineEnd);

                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available();

                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];

                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                   while (bytesRead > 0) {

                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                   // Responses from the server (code and message)
                respuestaServidor = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();

                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + respuestaServidor);

                   // Éxito
                   if(respuestaServidor == 200) {

                       runOnUiThread(new Runnable() {
                            public void run() {

                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                              +" http://www.androidexample.com/media/uploads/"
                                              +uploadFileName;

                                //messageText.setText(msg);
                                Toast.makeText(Chat.this, "File Upload Complete.",
                                             Toast.LENGTH_SHORT).show();
                            }
                        });
                   }

                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();

              } catch (MalformedURLException ex) {
                  ex.printStackTrace();

                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("MalformedURLException Exception : check script url.");
                          Toast.makeText(UploadToServer.this, "MalformedURLException",
                                                              Toast.LENGTH_SHORT).show();
                      }
                  });

                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
              } catch (Exception e) {
                  e.printStackTrace();

                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("Got Exception : see logcat ");
                          Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
                                  Toast.LENGTH_SHORT).show();
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "
                                                   + e.getMessage(), e);
              }
              // return respuestaServidor;

           } // End else block
        */

        /*
        httppost = new HttpPost(URL);
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("title", new StringBody("position.csv", Charset.forName("UTF-8")));
        File myFile = new File(Environment.getExternalStorageDirectory(), file);
        FileBody fileBody = new FileBody(myFile);
        entity.addPart("file", fileBody);
        httppost.setEntity(entity);
        httppost.getParams().setParameter("project", id);
        */
    }

    public void adjuntarArchivo (View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Elige el archivo"), 1);
    }



    public void playAudio (View v){
        if(v.getId() == R.id.boton_audio) {
            if(mp.isPlaying()) {
                // is playing
                mp.pause();
                botonAudio.setBackgroundResource(R.drawable.ic_play);
            } else {
                // on pause
                mp.start();
                botonAudio.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }

    class AdaptadorChat extends ArrayAdapter <Mensaje>{
        private Context contexto;
        private ArrayList<Mensaje> mensajes;
        private int cod_usuario;

        public AdaptadorChat( Context context, int resource, ArrayList<Mensaje> msj, int cod_usuario) {
            super(context, resource);
            this.contexto = context;
            this.mensajes = new ArrayList<>(msj);
            this.cod_usuario=cod_usuario;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Mensaje mensaje = mensajes.get(position);

            int layout_texto, layout_imagen, layout_audio;

            if (mensaje.getCod_emisor() != this.cod_usuario){
                System.out.println("Mensaje recibido");
                layout_texto = R.layout.texto_recibido;
                layout_imagen = R.layout.imagen_recibida;
                layout_audio = R.layout.audio_recibido;
            }
            else {
                System.out.println("Mensaje enviado");
                layout_texto = R.layout.texto_enviado;
                layout_imagen = R.layout.imagen_enviada;
                layout_audio = R.layout.audio_enviado;
            }

            if (mensaje.getContenido() != "null") {
                view = inflater.inflate(layout_texto, null);
                TextView texto = view.findViewById(R.id.texto_mensaje);
                texto.setText(mensaje.getContenido());
            }
            else if (mensaje.getMultimedia() != "null") {
                String ruta = mensaje.getMultimedia();
                String url = "http://test.dgp.esy.es/" + ruta;
                if (ruta.endsWith(".png") ||
                    ruta.endsWith(".jpg") ||
                    ruta.endsWith(".jpeg") ||
                    ruta.endsWith(".webm")) {
                    view = inflater.inflate(layout_imagen, null);
                    ImageView imagen = view.findViewById(R.id.imagen_mensaje);
                    Picasso.get().load(url).into(imagen);
                }
                else if (ruta.endsWith(".mp4")) {
                    view = inflater.inflate(layout_imagen, null);
                    cargarVideo(view, url);
                }
                else if (ruta.endsWith(".ogg")) {
                    view = inflater.inflate(layout_audio, null);
                    cargarAudio(view, url);
                }
            }

            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            TextView fecha = view.findViewById(R.id.fecha_mensaje);
            fecha.setText(formatoFecha.format(mensaje.getFecha()));

            return view;
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
            TextView texto = view.findViewById(R.id.texto_mensaje);

            if (entrada.getContenido() != "null") {
                texto.setText(entrada.getContenido());
            }
            else if (entrada.getMultimedia() != "null") {
                //texto.setVisibility(View.GONE);
                String ruta = entrada.getMultimedia();
                String url = "http://test.dgp.esy.es/" + ruta;
                if (entrada.getMultimedia().endsWith(".png") ||
                    entrada.getMultimedia().endsWith(".jpg") ||
                    entrada.getMultimedia().endsWith(".jpeg") ||
                    entrada.getMultimedia().endsWith(".webm")) {
                    ImageView imagen = view.findViewById(R.id.imagen_mensaje);
                    imagen.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(imagen);
                }
                else if (entrada.getMultimedia().endsWith(".mp4")) {
                    cargarVideo(view, url);
                }
                else if (entrada.getMultimedia().endsWith(".ogg")) {
                    cargarAudio(view, url);
                }
            }

            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            TextView fecha = view.findViewById(R.id.fecha_mensaje);
            fecha.setText(formatoFecha.format(entrada.getFecha()));
        }

        private void cargarVideo(View view, String url) {
            ImageView imagen = view.findViewById(R.id.imagen_mensaje);
            imagen.setImageResource(R.drawable.ic_play);
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Chat.this, VideoActivity.class);
                    intent.putExtra("url", url);
                    Chat.this.startActivity(intent);
                }
            });
        }

        private void cargarAudio(View view, String url){
            try {
                mp.reset();
                mp.setDataSource(url);
                mp.prepare();

                LinearLayout audio = view.findViewById(R.id.audio_mensaje);
                audio.setVisibility(View.VISIBLE);

                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // hide the actionbar
                        getSupportActionBar().hide();

                        tiempoAudio = findViewById(R.id.tiempo_audio);
                        duracionAudio = findViewById(R.id.duracion_audio);
                        barraAudio = findViewById(R.id.barra_audio);
                        botonAudio = findViewById(R.id.boton_audio);
                        botonAudio.setBackgroundResource(R.drawable.ic_play);

                        mp.setLooping(false);
                        mp.seekTo(0);
                        mp.setVolume(0.5f, 0.5f);

                        String duration = millisecondsToString(mp.getDuration());
                        duracionAudio.setText(duration);

                        barraAudio.setMax(mp.getDuration());
                        barraAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                                if(isFromUser) {
                                    mp.seekTo(progress);
                                    seekBar.setProgress(progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (mp != null) {
                                    if(mp.isPlaying()) {
                                        try {
                                            final double current = mp.getCurrentPosition();
                                            final String elapsedTime = millisecondsToString((int) current);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tiempoAudio.setText(elapsedTime);
                                                    barraAudio.setProgress((int) current);
                                                }
                                            });

                                            Thread.sleep(500);
                                        }catch (InterruptedException e) {}
                                    }
                                }
                            }
                        }).start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String millisecondsToString(int time) {
            String elapsedTime = "";
            int minutes = time / 1000 / 60;
            int seconds = time / 1000 % 60;
            elapsedTime = minutes+":";
            if(seconds < 10) {
                elapsedTime += "0";
            }
            elapsedTime += seconds;

            return  elapsedTime;
        }
    }
}
