package com.example.vality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
    String url="http://test.dgp.esy.es/app/login2.php";
    Usuario usuario;
    RequestQueue queue;
    String contrasenaIntroducida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = new Usuario();
        contrasenaIntroducida ="";
        queue = Volley.newRequestQueue(this);

        // Prueba para ver que el constructor para crear tareas a raíz de peticiones al servidor funciona
        // Probado con un servidor local
        /*
        TextView textView=(TextView)findViewById(R.id.tarea);
        Tarea t = new Tarea("http://192.168.0.124:3000/tarea", queue, textView);
         */

    }
    public void logueo (View v) throws InterruptedException {
        TextView textView=(TextView)findViewById(R.id.mensajeServer);

        usuario.iniciar(url, queue, textView, contrasenaIntroducida, this);
    }

    public void pulsadoArbol(View v){
        contrasenaIntroducida = contrasenaIntroducida + "arbol";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoAvion(View v){
        contrasenaIntroducida = contrasenaIntroducida + "avion";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoBalon(View v){
        contrasenaIntroducida = contrasenaIntroducida + "balon";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoBotella(View v){
        contrasenaIntroducida = contrasenaIntroducida + "botella";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoCoche(View v){
        contrasenaIntroducida = contrasenaIntroducida + "coche";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoLeon(View v){
        contrasenaIntroducida = contrasenaIntroducida + "leon";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoMoto(View v){
        contrasenaIntroducida = contrasenaIntroducida + "moto";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoOrdenador(View v){
        contrasenaIntroducida = contrasenaIntroducida + "ordenador";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoEnchufe(View v){
        contrasenaIntroducida = contrasenaIntroducida + "enchufe";
        v.setBackgroundColor(Color.GREEN);
        v.setClickable(false);
    }

    public void pulsadoBorrar(View v){
        borrarContrasena();
    }

    public void borrarContrasena(){
        contrasenaIntroducida = "";
        ImageButton arbol=(ImageButton)findViewById(R.id.botonImagenArbol);
        arbol.setBackgroundColor(Color.parseColor("#C3C2C2"));
        arbol.setClickable(true);

        ImageButton avion=(ImageButton)findViewById(R.id.botonImagenAvion);
        avion.setBackgroundColor(Color.parseColor("#C3C2C2"));
        avion.setClickable(true);

        ImageButton balon=(ImageButton)findViewById(R.id.botonImagenBalon);
        balon.setBackgroundColor(Color.parseColor("#C3C2C2"));
        balon.setClickable(true);

        ImageButton botella=(ImageButton)findViewById(R.id.botonImagenBotella);
        botella.setBackgroundColor(Color.parseColor("#C3C2C2"));
        botella.setClickable(true);

        ImageButton coche=(ImageButton)findViewById(R.id.botonImagenCoche);
        coche.setBackgroundColor(Color.parseColor("#C3C2C2"));
        coche.setClickable(true);

        ImageButton leon=(ImageButton)findViewById(R.id.botonImagenLeon);
        leon.setBackgroundColor(Color.parseColor("#C3C2C2"));
        leon.setClickable(true);

        ImageButton moto=(ImageButton)findViewById(R.id.botonImagenMoto);
        moto.setBackgroundColor(Color.parseColor("#C3C2C2"));
        moto.setClickable(true);

        ImageButton ordenador=(ImageButton)findViewById(R.id.botonImagenOrdenador);
        ordenador.setBackgroundColor(Color.parseColor("#C3C2C2"));
        ordenador.setClickable(true);

        ImageButton enchufe=(ImageButton)findViewById(R.id.botonImagenEnchufe);
        enchufe.setBackgroundColor(Color.parseColor("#C3C2C2"));
        enchufe.setClickable(true);
    }

    /*
    @Override
    public void onBackPressed() {
        if(estado_app == "LOGUEO" || estado_app == "LOGUEADO"){
            super.onBackPressed();
        }else{
            if(estado_app == "MOSTRARTAREASIMPLE"){
                this.setContentView(R.layout.listatareas);
                this.estado_app = "LOGUEADO";
            }else{

            }

        }
    }

 */
}