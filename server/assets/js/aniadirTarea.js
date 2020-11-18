let usuarios = [];
let grupos = [];

// ********************************
// Añadir y quitar usuarios
// ********************************

function aniadirUsuario(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.remove('basic');
    el.innerHTML = "Añadido";

    // Añade a la lista el usuario
    usuarios.push(cod_user);

    // Cambia la función onclick
    el.setAttribute('onclick', `quitarUsuario(this, ${cod_user})`);
}

function quitarUsuario(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.add('basic');
    el.innerHTML = "Añadir";

    // Quita de la lista el usuario
    var index = usuarios.indexOf(cod_user);
    if (index >= 0)
        usuarios.splice( index, 1 );

    // Cambia la función onclick
    el.setAttribute('onclick', `aniadirUsuario(this, ${cod_user})`);
}

// ********************************
// Añadir y quitar grupos
// ********************************


function aniadirGrupo(el, cod_grupo){
    // Vuelve el boton azul y pone añadido
    el.classList.remove('basic');
    el.innerHTML = "Añadido";

    // Añade a la lista el grupo
    grupos.push(cod_grupo);

    // Cambia la función onclick
    el.setAttribute('onclick', `quitarGrupo(this, ${cod_grupo})`);
}

function quitarGrupo(el, cod_grupo){
    // Vuelve el boton azul y pone añadido
    el.classList.add('basic');
    el.innerHTML = "Añadir";

    // Añade a la lista del grupo el usuario
    var index = grupos.indexOf(cod_grupo);
    if (index >= 0)
        grupos.splice( index, 1 );

    // Cambia la función onclick
    el.setAttribute('onclick', `aniadirGrupo(this, ${cod_grupo})`);
}


// Cuando se pulsa submit se comprueba que hay un nombre y al menos 1 usuario en el grupo
// y se crean los input para los usuarios para que los detecte PHP como un array
function hacerSubmit(){
    var nombre = document.getElementById('nombre-tarea').value;
    var comentario = document.getElementById('comentario-tarea').value;
    var warning = document.getElementById('warning-mensaje');
    var lista = document.getElementById('lista-errores');

    // Entra si no se ha introducido nombre, no se ha introducido comentario
    // o no se ha introducio ningún usuario y ningún grupo
    if(!nombre || !comentario || (!usuarios.length && !grupos.length)){
        warning.style.display = "block";
        lista.innerHTML = "";

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
        user.type = 'text';
        user.setAttribute("value",`${usuarios[i]}`);
        user.name = `usuarios[]`;
        user.style.display = "none";
    }


    // Añade los grupos
    for(i=0; i<grupos.length; i++){
        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.type = 'text';
        user.setAttribute("value",`${grupos[i]}`);
        user.name = `grupos[]`;
        user.style.display = "none";
    }    

    document.getElementById('warning-carga').style.display = "flex";

    return true;
}