package com.example.vality;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.vality.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    ArrayList<Tarea> tareas;
    private LayoutInflater mInflater;
    RequestQueue queue;
    private int diaSemana;
    private calendarioActivity general;
    int cod_usuario;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Tarea> tareas, RequestQueue queue, calendarioActivity general, int cod_usuario) {
        this.mInflater = LayoutInflater.from(context);
        this.tareas = tareas;
        this.queue=queue;
        this.general=general;
        this.cod_usuario=cod_usuario;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cargarImagen(holder.imagen, tareas.get(position).getPictograma(), tareas.get(position).getTitulo(), queue);
        holder.titulo.setText(tareas.get(position).getTitulo());
        holder.tarea=tareas.get(position);

        holder.imagen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("Has clickado " + tareas.get(position));
                Tarea elegida = tareas.get(position);

                Intent i = new Intent(general, unaTareaActivity.class);

                i.putExtra("cod_tarea", elegida.getCod_tarea() + "");
                i.putExtra("cod_facilitador", elegida.getCod_facilitador() + "");
                i.putExtra("titulo", elegida.getTitulo() + "");
                i.putExtra("descripcion", elegida.getDescripcion() + "");
                i.putExtra("fecha_limite", elegida.getFecha_limite() + "");
                i.putExtra("objetivo", elegida.getObjetivo() + "");
                i.putExtra("multimedia", elegida.getMultimedia() + "");
                i.putExtra("realizada", elegida.getRealizada() + "");
                i.putExtra("calificacion", elegida.getCalificacion() + "");
                i.putExtra("cod_usuario", cod_usuario + "");
                i.putExtra("pictograma", elegida.getPictograma() + "");
                general.startActivity(i);
            }
        });

        holder.titulo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("Has clickado " + tareas.get(position));
                Tarea elegida = tareas.get(position);

                Intent i = new Intent(general, unaTareaActivity.class);

                i.putExtra("cod_tarea", elegida.getCod_tarea() + "");
                i.putExtra("cod_facilitador", elegida.getCod_facilitador() + "");
                i.putExtra("titulo", elegida.getTitulo() + "");
                i.putExtra("descripcion", elegida.getDescripcion() + "");
                i.putExtra("fecha_limite", elegida.getFecha_limite() + "");
                i.putExtra("objetivo", elegida.getObjetivo() + "");
                i.putExtra("multimedia", elegida.getMultimedia() + "");
                i.putExtra("realizada", elegida.getRealizada() + "");
                i.putExtra("calificacion", elegida.getCalificacion() + "");
                i.putExtra("cod_usuario", cod_usuario + "");
                i.putExtra("pictograma", elegida.getPictograma() + "");
                general.startActivity(i);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;
        Tarea tarea;

        ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.ImagenTarea);
            titulo = itemView.findViewById(R.id.tituloTarea);
        }
    }

    // convenience method for getting data at click position
    public Tarea getItem(int id) {
        return tareas.get(id);
    }

    private void cargarImagen(ImageView cuadroImagen, String multimedia, String titulo, RequestQueue queue){
        String url="http://test.dgp.esy.es/"+ multimedia;
        System.out.println("Creamos mensaje para pedir imagen: "+ multimedia);

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        cuadroImagen.setImageBitmap(response);
                        cuadroImagen.setContentDescription(titulo);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: "+ error);
            }
        });
        queue.add(imageRequest);
    }
}