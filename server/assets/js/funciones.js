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
    //Mostrar o no mostrar las tareas
    let tarjetas = document.querySelectorAll('.ui .card');
    for(i=num; i<tarjetas.length; i++){
        switch(estado){
            case "T":
                //Poner todos los display a ""
                break;
            case "C":
                //Poner los display de las NC a "none"
                break:
            case "NC":
                //Poner los display de las C a "none"
                break
        }
    }
}
