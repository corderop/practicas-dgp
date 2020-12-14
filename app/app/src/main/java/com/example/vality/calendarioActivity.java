package com.example.vality;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class calendarioActivity extends AppCompatActivity {
    Usuario usuario;
    RequestQueue queue;
    ArrayList <Tarea> tareas;
    int diaActual;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        tareas = new ArrayList<Tarea>();
        queue = Volley.newRequestQueue(this);
        usuario.setNombre(getIntent().getStringExtra("nombre"));
        usuario.setContrasena(getIntent().getStringExtra("contrasena"));
        usuario.setCod_usuario(Integer.parseInt(getIntent().getStringExtra("cod_usuario")));
        setContentView(R.layout.calendario);
        usuario.obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue, this, "NULL");
        tareas = usuario.getTareas();
        Calendar c1 = Calendar.getInstance();
        diaActual = c1.get(Calendar.DATE);
        System.out.println("Fecha actual: "+ diaActual);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
