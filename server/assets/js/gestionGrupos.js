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