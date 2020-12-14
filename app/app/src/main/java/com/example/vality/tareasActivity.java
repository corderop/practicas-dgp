package com.example.vality;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public void pulsadoCalendario(){
        Intent i = new Intent(this, calendarioActivity.class);
        i.putExtra("nombre", usuario.getNombre());
        i.putExtra("contrasena", usuario.getContrasena());
        i.putExtra("cod_usuario", usuario.getCod_usuario()+"");
        this.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_calendario:
                pulsadoCalendario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
