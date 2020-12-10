package com.example.vality;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
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
import java.util.Timer;
import java.util.TimerTask;

public class unaTareaActivity extends AppCompatActivity {
    Tarea tarea;
    RequestQueue queue;
    int cod_usuario;

    TextView tvTime, tvDuration;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;
    MediaPlayer mp;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date aux = new Date();
        mp = new MediaPlayer();

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

        this.cod_usuario = Integer.parseInt(getIntent().getStringExtra("cod_usuario"));

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
        try {
            mp.setDataSource("http://test.dgp.esy.es/"+ tarea.getMultimedia());
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // hide the actionbar
                    getSupportActionBar().hide();

                    tvTime = findViewById(R.id.tvTime);
                    tvDuration = findViewById(R.id.tvDuration);
                    seekBarTime = findViewById(R.id.seekBarTime);
                    btnPlay = findViewById(R.id.btnPlay);
                    btnPlay.setBackgroundResource(R.drawable.ic_play);

                    mp.setLooping(true);
                    mp.seekTo(0);
                    mp.setVolume(0.5f, 0.5f);

                    String duration = millisecondsToString(mp.getDuration());
                    tvDuration.setText(duration);

                    seekBarTime.setMax(mp.getDuration());
                    seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                                                tvTime.setText(elapsedTime);
                                                seekBarTime.setProgress((int) current);
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

    public void accionBoton(View v){
        if(v.getId() == R.id.btnPlay) {
            if(mp.isPlaying()) {
                // is playing
                mp.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            } else {
                // on pause
                mp.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
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

    public void abrir_chat(View view) {
        Intent i = new Intent(this, Chat.class);
        i.putExtra("cod_usuario", cod_usuario+"");
        i.putExtra("cod_tarea", tarea.getCod_tarea()+"");
        this.startActivity(i);
    }
}
