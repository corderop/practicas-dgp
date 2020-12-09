package com.example.vality;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity {

    ListView listView;
    AdaptadorChat adaptador;
    ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.chat);

        Date fecha = new Date(2020, 12, 7);
        Mensaje msj1 = new Mensaje(1, fecha, "Hola, ¿qué tal?", "", false, 1);
        Mensaje msj2 = new Mensaje(1, fecha, "Bien, ¿y tú?", "", false, 1);

        mensajes.add(msj1);
        mensajes.add(msj2);

        listView = findViewById(R.id.list);
        adaptador = new AdaptadorChat(this, R.layout.mensaje_recibido, mensajes);
        listView.setAdapter(adaptador);
    }

    class AdaptadorChat extends ArrayAdapter<Mensaje> {
        Context contexto;
        ArrayList<String> textos = new ArrayList<>();
        ArrayList<Date> fechas = new ArrayList<>();

        public AdaptadorChat(@NonNull Context context, int resource, List<Mensaje> mensajes) {
            super(context, resource);
            contexto = context;
            for (int i = 0 ; i < mensajes.size() ; i++) {
                textos.add(mensajes.get(i).getContenido());
                fechas.add(mensajes.get(i).getFecha());
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mensaje = layoutInflater.inflate(R.layout.mensaje_recibido, parent, false);
            TextView miTexto = mensaje.findViewById(R.id.texto_mensaje);
            TextView miFecha = mensaje.findViewById(R.id.fecha_mensaje);

            miTexto.setText(textos.get(position));
            miFecha.setText(fechas.get(position).toString());

            return mensaje;
        }
    }
}
