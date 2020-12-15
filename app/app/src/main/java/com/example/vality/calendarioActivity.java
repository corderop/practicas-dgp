package com.example.vality;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class calendarioActivity extends AppCompatActivity {
    Usuario usuario;
    RequestQueue queue;
    int diaActual;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        queue = Volley.newRequestQueue(this);
        usuario.setNombre(getIntent().getStringExtra("nombre"));
        usuario.setContrasena(getIntent().getStringExtra("contrasena"));
        usuario.setCod_usuario(Integer.parseInt(getIntent().getStringExtra("cod_usuario")));
        setContentView(R.layout.calendario);
        usuario.obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue, this, "NULL");

        /*
        Calendar c1 = Calendar.getInstance();
        diaActual = c1.get(Calendar.DATE);
        System.out.println("Fecha actual: "+ diaActual);
         */
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
