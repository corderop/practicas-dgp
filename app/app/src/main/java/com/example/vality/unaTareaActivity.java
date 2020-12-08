package com.example.vality;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class unaTareaActivity extends AppCompatActivity {
    Tarea tarea;
    RequestQueue queue;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date aux = new Date();
        try {
            aux = formatoFecha.parse(getIntent().getStringExtra("fecha_limite"));
        }catch (ParseException ex){
            System.out.println(ex);
        }
        tarea = new Tarea(
                Integer.parseInt(getIntent().getStringExtra("cod_tarea")),
                Integer.parseInt(getIntent().getStringExtra("cod_facilitador")),
                getIntent().getStringExtra("titulo"),
                getIntent().getStringExtra("descripcion"),
                aux,
                getIntent().getStringExtra("objetivo"),
                getIntent().getStringExtra("multimedia"),
                Boolean.parseBoolean(getIntent().getStringExtra("realizada")),
                Integer.parseInt(getIntent().getStringExtra("calificacion"))

        );
        queue = Volley.newRequestQueue(this);

        setContentView(R.layout.pantalla_tarea);
        TextView titulo = this.findViewById(R.id.tituloTarea);
        titulo.setText(this.tarea.getTitulo());

        if(tarea.getMultimedia().endsWith("webp") || tarea.getMultimedia().endsWith("png") || tarea.getMultimedia().endsWith("jpg") || tarea.getMultimedia().endsWith("jpeg")){
            cargarImagen();
        }else{
            if(tarea.getMultimedia().endsWith("mp4")){
                cargarVideo();
            }else{
                if(tarea.getMultimedia().endsWith("ogg")) {
                    cargarAudio();
                }
            }
        }
    }

    private void cargarImagen(){
        String url="http://test.dgp.esy.es/"+ tarea.getMultimedia();
        ImageView imagen = this.findViewById(R.id.imagenTarea);
        imagen.setVisibility(View.VISIBLE);

        System.out.println("Creamos mensaje para pedir imagen: "+ tarea.getMultimedia());

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
        });
        queue.add(imageRequest);
    }

    private void cargarVideo() {
        VideoView videoView =(VideoView)findViewById(R.id.videoTarea);
        videoView.setVisibility(View.VISIBLE);

        System.out.println("Creamos mensaje para pedir video: " + tarea.getMultimedia());

        videoView.setVideoURI(Uri.parse("http://test.dgp.esy.es/"+ tarea.getMultimedia()));
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                System.out.println("Iniciamos video");
                videoView.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(unaTareaActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
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

    private void cargarAudio(){

    }
}
