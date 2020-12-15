package com.example.vality;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ItemClickListener mClickListener;
    RequestQueue queue;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Tarea> tareas, RequestQueue queue) {
        this.mInflater = LayoutInflater.from(context);
        this.tareas = tareas;
        this.queue=queue;
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
        cargarImagen(holder.imagen, tareas.get(position).getPictograma(), queue);
        holder.titulo.setText(tareas.get(position).getTitulo());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagen;
        TextView titulo;

        ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.ImagenTarea);
            titulo = itemView.findViewById(R.id.tituloTarea);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Tarea getItem(int id) {
        return tareas.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    private void cargarImagen(ImageView cuadroImagen, String multimedia, RequestQueue queue){
        String url="http://test.dgp.esy.es/"+ multimedia;
        System.out.println("Creamos mensaje para pedir imagen: "+ multimedia);

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        cuadroImagen.setImageBitmap(response);
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