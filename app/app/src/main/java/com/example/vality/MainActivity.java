package com.example.vality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        // Prueba para ver que el constructor para crear tareas a raíz de peticiones al servidor funciona
        // Probado con un servidor local
        TextView textView=(TextView)findViewById(R.id.tarea);
        Tarea t = new Tarea("http://192.168.0.124:3000/tarea", queue, textView);

    }
}