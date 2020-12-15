package com.example.vality;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    private MyRecyclerViewAdapter adaptadorLunes;
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

    public void setAdapterDia(MyRecyclerViewAdapter nuevoAdapter, ArrayList<Tarea> tareas){
        this.adaptadorLunes=nuevoAdapter;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
