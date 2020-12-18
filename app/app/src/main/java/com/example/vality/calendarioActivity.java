package com.example.vality;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
    int semanaActual;
    ArrayList<Tarea> tareas;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        queue = Volley.newRequestQueue(this);
        usuario.setNombre(getIntent().getStringExtra("nombre"));
        usuario.setContrasena(getIntent().getStringExtra("contrasena"));
        usuario.setCod_usuario(Integer.parseInt(getIntent().getStringExtra("cod_usuario")));
        setContentView(R.layout.calendario);
        tareas=new ArrayList<Tarea>();
        usuario.obtenerTareas("http://test.dgp.esy.es/app/tareas.php", queue, this, "NULL");

        /*
        Calendar c1 = Calendar.getInstance();
        diaActual = c1.get(Calendar.DATE);
        System.out.println("Fecha actual: "+ diaActual);
         */
    }

    private void setAdapterDia(MyRecyclerViewAdapter nuevoAdapter, ArrayList<Tarea> tareas){
        this.adaptadorLunes=nuevoAdapter;
    }

    public void actualizarAdaptadorDia(int dia, ArrayList<Tarea> tareasDia){
        RecyclerView recyclerView = null;
        if(dia==1) {
            System.out.println("Creamos lista del lunes");
                LinearLayout aux = this.findViewById(R.id.seccionLunes);
                if(tareasDia.size()!=0) {
                    aux.setVisibility(View.VISIBLE);
                }else{
                    aux.setVisibility(View.GONE);
                }
                recyclerView = this.findViewById(R.id.listaTareasLunes);

        }else{
            if(dia==2) {
                System.out.println("Creamos lista del martes");
                LinearLayout aux = this.findViewById(R.id.seccionMartes);
                if(tareasDia.size()!=0) {
                    aux.setVisibility(View.VISIBLE);
                }else{
                    aux.setVisibility(View.GONE);
                }
                recyclerView = this.findViewById(R.id.listaTareasMartes);
            }else{
                if(dia==3) {
                    System.out.println("Creamos lista del miercoles");
                    LinearLayout aux = this.findViewById(R.id.seccionMiercoles);
                    if(tareasDia.size()!=0) {
                        aux.setVisibility(View.VISIBLE);
                    }else{
                        aux.setVisibility(View.GONE);
                    }
                    recyclerView = this.findViewById(R.id.listaTareasMiercoles);
                }else{
                    if(dia==4) {
                        System.out.println("Creamos lista del Jueves");
                        LinearLayout aux = this.findViewById(R.id.seccionJueves);
                        if(tareasDia.size()!=0) {
                            aux.setVisibility(View.VISIBLE);
                        }else{
                            aux.setVisibility(View.GONE);
                        }
                        recyclerView = this.findViewById(R.id.listaTareasJueves);
                    }else{
                        if(dia==5) {
                            System.out.println("Creamos lista del Viernes");
                            LinearLayout aux = this.findViewById(R.id.seccionViernes);
                            if(tareasDia.size()!=0) {
                                aux.setVisibility(View.VISIBLE);
                            }else{
                                aux.setVisibility(View.GONE);
                            }
                            recyclerView = this.findViewById(R.id.listaTareasViernes);
                        }else{
                            if(dia==6) {
                                System.out.println("Creamos lista del Sabado");
                                LinearLayout aux = this.findViewById(R.id.seccionSabado);
                                if(tareasDia.size()!=0) {
                                    aux.setVisibility(View.VISIBLE);
                                }else{
                                    aux.setVisibility(View.GONE);
                                }
                                recyclerView = this.findViewById(R.id.listaTareasSabado);
                            }else{
                                if(dia==7) {
                                    System.out.println("Creamos lista del Domingo");
                                    LinearLayout aux = this.findViewById(R.id.seccionDomingo);
                                    if(tareasDia.size()!=0) {
                                        aux.setVisibility(View.VISIBLE);
                                    }else{
                                        aux.setVisibility(View.GONE);
                                    }
                                    recyclerView = this.findViewById(R.id.listaTareasDomingo);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(recyclerView !=null){
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);

            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, tareasDia, queue, (calendarioActivity)this, usuario.getCod_usuario());   //Lunes
            ((calendarioActivity)this).setAdapterDia(adapter, tareasDia);
            recyclerView.setAdapter(adapter);
        }
    }

    public void setTareasSemana(ArrayList<Tarea> tareas, int semanaActual){
        this.tareas=tareas;
        this.semanaActual=semanaActual;
    }

    public void semanaAnterior(View v){
        semanaActual= semanaActual -1;
        if(semanaActual==0){
            semanaActual=53;
        }
        actualizarSemana(semanaActual);
    }

    public void semanaSiguiente(View v){
        semanaActual = semanaActual%53 +1;
        actualizarSemana(semanaActual);
    }

    public void actualizarSemana(int semana){
        Date diaActual = (Date) Calendar.getInstance().getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek( Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek( 4 );
        calendar.setTime(diaActual);
        int semanaTarea;

        System.out.println("Pasamos a crear la lista de la semana "+ semana);
        ArrayList<Tarea> tareasLunes = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasMartes = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasMiercoles = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasJueves = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasViernes = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasSabado = new ArrayList<Tarea>();
        ArrayList<Tarea> tareasDomingo = new ArrayList<Tarea>();
        for(Tarea tarea : tareas){
            calendar.setTime(tarea.getFecha_limite());
            semanaTarea = calendar.get(Calendar.WEEK_OF_YEAR);
            System.out.println("Tarea "+ tarea.getTitulo()+ " pertenece a la semana: "+ semanaTarea);
            if(semana == semanaTarea){
                System.out.print("Añadimos tarea a la semana");
                switch (calendar.get(Calendar.DAY_OF_WEEK)){
                    case 1:
                        System.out.println(" en domingo.");
                        tareasDomingo.add(tarea);
                        break;
                    case 2:
                        System.out.println(" en lunes.");
                        tareasLunes.add(tarea);
                        break;
                    case 3:
                        System.out.println(" en martes.");
                        tareasMartes.add(tarea);
                        break;
                    case 4:
                        System.out.println(" en miercoles.");
                        tareasMiercoles.add(tarea);
                        break;
                    case 5:
                        System.out.println(" en jueves.");
                        tareasJueves.add(tarea);
                        break;
                    case 6:
                        System.out.println(" en viernes.");
                        tareasViernes.add(tarea);
                        break;
                    case 7:
                        System.out.println(" en sabado.");
                        tareasSabado.add(tarea);
                        break;
                }
            }
        }
        actualizarAdaptadorDia(1,tareasLunes);
        actualizarAdaptadorDia(2,tareasMartes);
        actualizarAdaptadorDia(3,tareasMiercoles);
        actualizarAdaptadorDia(4,tareasJueves);
        actualizarAdaptadorDia(5,tareasViernes);
        actualizarAdaptadorDia(6,tareasSabado);
        actualizarAdaptadorDia(7,tareasDomingo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
