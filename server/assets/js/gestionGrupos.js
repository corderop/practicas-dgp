let usuariosGrupos = [];

function aniadirUsuarioGrupo(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.remove('basic');
    el.innerHTML = "Añadido";

    // Añade a la lista del grupo el usuario
    usuariosGrupos.push(cod_user);

    // Cambia la función onclick
    el.setAttribute('onclick', `quitarUsuarioGrupo(this, ${cod_user})`);
}

function quitarUsuarioGrupo(el, cod_user){
    // Vuelve el boton azul y pone añadido
    el.classList.add('basic');
    el.innerHTML = "Añadir";

    // Añade a la lista del grupo el usuario
    var index = usuariosGrupos.indexOf(cod_user);
    if (index >= 0)
        usuariosGrupos.splice( index, 1 );

    // Cambia la función onclick
    el.setAttribute('onclick', `aniadirUsuarioGrupo(this, ${cod_user})`);
}

// Cuando se pulsa submit se comprueba que hay un nombre y al menos 1 usuario en el grupo
// y se crean los input para los usuarios para que los detecte PHP como un array
function hacerSubmit(){
    var nombre = document.getElementById('nombre-grupo').value;
    var warning = document.getElementById('warning-mensaje');
    var lista = document.getElementById('lista-errores');

    // Entra si el nombre no es correcto o no se han seleccionado usuarios
    if(!nombre || !usuariosGrupos.length){ 
        warning.style.display = "block";
        lista.innerHTML = "";

        if(!nombre){
            var li = document.createElement('li');
            li.innerHTML = "Introducir un nombre";
            lista.appendChild(li);
        }

        if(!usuariosGrupos.length){
            var li = document.createElement('li');
            li.innerHTML = "Añadir al menos un usuario al grupo";
            lista.appendChild(li);
        }

        // returnToPreviousPage();
        return false;
    }

    // Añade los usuarios como input para que los detecte PHP
    let formulario = document.getElementById('modificar-grupo');
    
    for(i=0; i<usuariosGrupos.length; i++){
        var input = document.createElement("input");
        var user = formulario.appendChild(input);
        user.type = 'text';
        user.setAttribute("value",`${usuariosGrupos[i]}`);
        user.name = `usuarios[]`;
        user.style.display = "none";
    }

    return true;
}