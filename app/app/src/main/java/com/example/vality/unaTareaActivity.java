package com.example.vality;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
    }
}
