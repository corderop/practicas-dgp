package com.example.vality;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
        usuario.obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue, this, "LISTA");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void pulsadoCalendario(View v){
        Intent i = new Intent(this, calendarioActivity.class);
        i.putExtra("nombre", usuario.getNombre());
        i.putExtra("contrasena", usuario.getContrasena());
        i.putExtra("cod_usuario", usuario.getCod_usuario()+"");
        this.startActivity(i);
    }

}
