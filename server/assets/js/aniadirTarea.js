let usuarios = [];
let fechas_usuarios = [];
let grupos = [];
let fechas_grupos = [];

// ********************************
// Añadir y quitar usuarios
// ********************************

function aniadirUsuario(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.remove('basic');
    el.innerHTML = "Añadido";
    fecha = document.getElementById(`fecha-usuario-${cod_user}`);

    // Añade a la lista el usuario
    usuarios.push(cod_user);
    fechas_usuarios.push(fecha.value);

    // Cambia la función onclick
    el.setAttribute('onclick', `quitarUsuario(this, ${cod_user})`);
}

function quitarUsuario(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.add('basic');
    el.innerHTML = "Añadir";

    // Quita de la lista el usuario
    var index = usuarios.indexOf(cod_user);
    if (index >= 0){
        usuarios.splice( index, 1 );
        fechas_usuarios.splice( index, 1);
    }

    // Cambia la función onclick
    el.setAttribute('onclick', `aniadirUsuario(this, ${cod_user})`);
}

function cambiarFechaUsuario(el, cod_user){
    var index = usuarios.indexOf(cod_user);
    if (index >= 0){
        fechas_usuarios[index] = el.value;
    }
}

// ********************************
// Añadir y quitar grupos
// ********************************


function aniadirGrupo(el, cod_grupo){
    // Vuelve el boton azul y pone añadido
    el.classList.remove('basic');
    el.innerHTML = "Añadido";
    fecha = document.getElementById(`fecha-grupo-${cod_grupo}`);

    // Añade a la lista el grupo
    grupos.push(cod_grupo);
    fechas_grupos.push(fecha.value);

    // Cambia la función onclick
    el.setAttribute('onclick', `quitarGrupo(this, ${cod_grupo})`);
}

function quitarGrupo(el, cod_grupo){
    // Vuelve el boton azul y pone añadido
    el.classList.add('basic');
    el.innerHTML = "Añadir";

    // Añade a la lista del grupo el usuario
    var index = grupos.indexOf(cod_grupo);
    if (index >= 0){
        grupos.splice( index, 1 );
        fechas_grupos.splice( index, 1);
    }

    // Cambia la función onclick
    el.setAttribute('onclick', `aniadirGrupo(this, ${cod_grupo})`);
}

function cambiarFechaGrupo(el, cod_grupo){
    console.log(`fecha cambiada grupo ${cod_grupo}`);
    var index = grupos.indexOf(cod_grupo);
    if (index >= 0){
        fechas_grupos[index] = el.value;
    }
}


// Cuando se pulsa submit se comprueba que hay un nombre y al menos 1 usuario en el grupo
// y se crean los input para los usuarios para que los detecte PHP como un array
function hacerSubmit(){
    var nombre = document.getElementById('nombre-tarea').value;
    var comentario = document.getElementById('comentario-tarea').value;
    var warning = document.getElementById('warning-mensaje');
    var lista = document.getElementById('lista-errores');
    lista.innerHTML = "";
    warning.style.display = "none";

    // Entra si no se ha introducido nombre, no se ha introducido comentario
    // o no se ha introducio ningún usuario y ningún grupo
    if(!nombre || !comentario || (!usuarios.length && !grupos.length)){
        warning.style.display = "block";

        if(!nombre){
            var li = document.createElement('li');
            li.innerHTML = "Introducir un nombre para la tarea";
            lista.appendChild(li);
        }

        if(!comentario){
            var li = document.createElement('li');
            li.innerHTML = "Introducir un comentario para la tarea";
            lista.appendChild(li);
        }

        if(!usuarios.length && !grupos.length){
            var li = document.createElement('li');
            li.innerHTML = "Añadir al menos un usuario o un grupo a la tarea";
            lista.appendChild(li);
        }

        return false;
    }

    // Añade los usuarios y grupos como inputs para que se pasen por php
    let formulario = document.getElementById('aniadir-tarea');
    
    // Añade los usuarios
    for(i=0; i<usuarios.length; i++){
        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.classList.add('entrada');
        user.type = 'text';
        user.setAttribute("value",`${usuarios[i]}`);
        user.name = `usuarios[]`;
        user.style.display = "none";
    }

    //Añade las fechas de los usuarios
    for(i=0; i<fechas_usuarios.length; i++){
        if(fechas_usuarios[i] == ""){
            cambiarFechaUsuario(document.getElementById(`fecha-usuario-${usuarios[i]}`), usuarios[i]);
            if(fechas_usuarios[i] == ""){
                warning.style.display = "block";
                var li = document.createElement('li');
                li.innerHTML = "Hay usuarios que no tienen fecha asignada";
                lista.appendChild(li);

                let por_elimianr = document.getElementsByClassName("entrada");

                for(j=0; j<por_elimianr.length; j++){
                    por_elimianr[j].parentNode.removeChild(por_elimianr[j]);
                }

                return false;
            }
        }

        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.classList.add('entrada');
        user.type = 'text';
        user.setAttribute("value",`${fechas_usuarios[i]}`);
        user.name = `fechas_usuarios[]`;
        user.style.display = "none";
    }

    // Añade los grupos
    for(i=0; i<grupos.length; i++){
        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.classList.add('entrada');
        user.type = 'text';
        user.setAttribute("value",`${grupos[i]}`);
        user.name = `grupos[]`;
        user.style.display = "none";
    }    

    //Añade las fechas de los grupos
    for(i=0; i<fechas_grupos.length; i++){
        if(fechas_grupos[i] == ""){
            cambiarFechaGrupo(document.getElementById(`fecha-grupo-${grupos[i]}`), grupos[i]);
            if(fechas_grupos[i] == ""){
                warning.style.display = "block";
                var li = document.createElement('li');
                li.innerHTML = "Hay grupos que no tienen fecha asignada";
                lista.appendChild(li);

                let por_elimianr = document.getElementsByClassName("entrada");

                for(j=0; j<por_elimianr.length; j++){
                    por_elimianr[j].parentNode.removeChild(por_elimianr[j]);
                }

                return false;
            }
        }

        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.classList.add('entrada');
        user.type = 'text';
        user.setAttribute("value",`${fechas_grupos[i]}`);
        user.name = `fechas_grupos[]`;
        user.style.display = "none";
    }

    document.getElementById('warning-carga').style.display = "flex";

    return true;
}