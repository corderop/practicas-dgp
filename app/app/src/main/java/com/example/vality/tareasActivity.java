package com.example.vality;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class tareasActivity extends AppCompatActivity {
    Usuario usuario;
    RequestQueue queue;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        queue = Volley.newRequestQueue(this);
        usuario.setNombre(getIntent().getStringExtra("nombre"));
        usuario.setContrasena(getIntent().getStringExtra("contrasena"));
        usuario.setCod_usuario(Integer.parseInt(getIntent().getStringExtra("cod_usuario")));
        setContentView(R.layout.listatareas);
        usuario.obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue, this);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
