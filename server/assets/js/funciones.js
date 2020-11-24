/**
 * Filtra a las tarjetas en base a lo introducido en la barra de búsqueda
 * @param {*} busq Objeto input en html. Usar el parámetro this al llamarla en HTML.
 * @param {*} num Tarjeta en la que quieres que empiece. Normalmente 0. Será 1 en las ocasiones
 * en las que exista una tarjeta que sirva para añadir tareas etc.
 */
function buscar(busq, num){
    // Toma el valor introducido en la barra
    let valor = busq.value.toLowerCase();
    // Toma las tarjetas
    let tarjetas = document.querySelectorAll('.ui .card');

    if(valor.length > 3){
        for(i=num; i<tarjetas.length; i++){
            if(tarjetas[i].textContent.trim().toLowerCase().includes(valor))
                tarjetas[i].style.display = "";
            else
                tarjetas[i].style.display = "none";
        }
    }
    // Para que se muestren todas las tarjetas cuando borra la búsqueda
    else{
        for(i=num; i<tarjetas.length; i++){
            tarjetas[i].style.display = "";
        }
    }

}

function filtroCorregidas(element) {
    let estado = element.value;

    if(estado == "T"){
        let tarjetas = document.querySelectorAll('.ui .card');
        for(i=0; i<tarjetas.length; i++){
            tarjetas[i].style.display = '';
        }
    }
    else if(estado == "C"){
        let tarjetas_corregidas = document.querySelectorAll('.realizada');
        let tarjetas_no_corregidas = document.querySelectorAll('.no-realizada');

        for(i=0; i<tarjetas_corregidas.length; i++){
            tarjetas_corregidas[i].style.display = '';
        }
        for(i=0; i<tarjetas_no_corregidas.length; i++){
            tarjetas_no_corregidas[i].style.display = 'none';
        }
    }
    else if(estado == "NC"){
        let tarjetas_corregidas = document.querySelectorAll('.realizada');
        let tarjetas_no_corregidas = document.querySelectorAll('.no-realizada');
        
        for(i=0; i<tarjetas_corregidas.length; i++){
            tarjetas_corregidas[i].style.display = 'none';
        }
        for(i=0; i<tarjetas_no_corregidas.length; i++){
            tarjetas_no_corregidas[i].style.display = '';
        }
    }
}

function comprobarPass() {
    //alert("Funciona");
    let comprobada = false;
    let pass = document.getElementById("input-fotos").value;
    console.log("pass: " + pass);
    $.ajax({
        url:"comprobarPass.php",
        method:"POST",
        data:{imagenes:pass},
        success:function(data)
        {
            console.log("Resultado: " + data);
            if(data === "true"){
                console.log("Es true");
                //TODO que meta otra
            }
            else{
                console.log("Es false");
                //TODO enseñar guardar
            }
        }
    });
}
