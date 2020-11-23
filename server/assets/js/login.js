function cambiar(enlace, persona) {
    if (persona) {
        document.getElementById("login-normal").style.display = "none";
        document.getElementById("login-fotos").style.display = "block";
        enlace.innerHTML = "Iniciar sesión como administrador o facilitador";
        enlace.setAttribute('onclick', 'cambiar(this, false)');
    }
    else {
        document.getElementById("login-normal").style.display = "block";
        document.getElementById("login-fotos").style.display = "none";
        enlace.innerHTML = "Iniciar sesión como persona";
        enlace.setAttribute('onclick', 'cambiar(this, true)');
    }
}

function aniadirContrasena( foto, texto ){
    if(foto.getAttribute('data-seleccionada') == "no"){
        document.getElementById('input-fotos').value += `${texto}`;
        foto.style.backgroundColor = "#99ff66";
        foto.setAttribute('data-seleccionada', 'si')
    }
}

function borrarContrasena(){
    document.getElementById('input-fotos').value = '';
    var imagenes = document.getElementsByClassName('imagen-selector');
    for(i=0; i<imagenes.length; i++){
        imagenes[i].style.backgroundColor = "white";
        imagenes[i].setAttribute('data-seleccionada', 'no');
    }
}