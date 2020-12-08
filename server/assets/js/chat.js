function hacerSubmitChat(){
    var nombre = document.getElementById('mensaje-envio').value;
    var warning = document.getElementById('warning-mensaje');
    var archivos = document.getElementById('multimedia-envio').files.length;

    if(!nombre && !archivos){ 
        warning.style.display = "block";

        return false;
    }

    return true;
}